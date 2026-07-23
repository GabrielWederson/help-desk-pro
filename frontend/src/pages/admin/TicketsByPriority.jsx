import { useState } from 'react';
import { ticketService } from '../../api/ticketService';
import Pagination from '../../components/Pagination';
import toast from 'react-hot-toast';

const priorities = ['HIGH', 'MEDIUM', 'LOW'];

const TicketsByPriority = () => {
  const [selectedPriority, setSelectedPriority] = useState('');
  const [tickets, setTickets] = useState([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(false);

  const fetchTickets = async (priority, pageNum = 0) => {
    if (!priority) return;
    setLoading(true);
    try {
      const res = await ticketService.admin.getByPriority(priority, pageNum);
      console.log('Resposta getByPriority:', res.data); // temporário para depuração
      // Normaliza os tickets, aceitando content ou array direto
      const list = Array.isArray(res.data) ? res.data : (res.data.content || []);
      setTickets(list);
      setTotalPages(res.data.totalPages || 0);
      setPage(res.data.number || 0);
    } catch (err) {
      console.error('Erro ao buscar por prioridade:', err.response);
      toast.error(err.response?.data?.detail || 'Erro ao buscar tickets.');
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e) => {
    const p = e.target.value;
    setSelectedPriority(p);
    if (p) fetchTickets(p, 0);
  };

  return (
    <div>
      <h2 className="text-2xl font-bold mb-4">Buscar Tickets por Prioridade</h2>
      <div className="mb-4">
        <label className="block text-sm font-medium text-gray-700">Selecione a prioridade</label>
        <select
          value={selectedPriority}
          onChange={handleChange}
          className="mt-1 block w-64 px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
        >
          <option value="">-- Escolha --</option>
          {priorities.map((p) => (
            <option key={p} value={p}>{p}</option>
          ))}
        </select>
      </div>

      {loading && <div className="text-center py-8">Carregando...</div>}

      {!loading && selectedPriority && (
        <div className="overflow-x-auto bg-white shadow rounded-lg">
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Nome</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Tipo</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Status</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Data Criação</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-200">
              {tickets.length > 0 ? (
                tickets.map((ticket) => (
                  <tr key={ticket.id}>
                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{ticket.name}</td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{ticket.type}</td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{ticket.status}</td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                      {new Date(ticket.createdAt).toLocaleDateString()}
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="4" className="px-6 py-4 text-center text-sm text-gray-500">
                    Nenhum ticket encontrado para esta prioridade.
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      )}

      {totalPages > 0 && (
        <Pagination page={page} totalPages={totalPages} onPageChange={(p) => fetchTickets(selectedPriority, p)} />
      )}
    </div>
  );
};

export default TicketsByPriority;