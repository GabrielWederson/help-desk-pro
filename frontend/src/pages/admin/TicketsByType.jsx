import { useState } from 'react';
import { ticketService } from '../../api/ticketService';
import Pagination from '../../components/Pagination';
import toast from 'react-hot-toast';

const types = ['INCIDENT', 'PROBLEM', 'CHANGE_REQUEST', 'SERVICE_REQUEST'];

const TicketsByType = () => {
  const [selectedType, setSelectedType] = useState('');
  const [tickets, setTickets] = useState([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(false);

  const fetchTickets = async (type, pageNum = 0) => {
    if (!type) return;
    setLoading(true);
    try {
      const res = await ticketService.admin.getByType(type, pageNum);
      setTickets(res.data.content);
      setTotalPages(res.data.totalPages);
      setPage(res.data.number);
    } catch (err) {
      toast.error('Erro ao buscar tickets.');
    } finally {
      setLoading(false);
    }
  };

  const handleTypeChange = (e) => {
    const type = e.target.value;
    setSelectedType(type);
    if (type) fetchTickets(type, 0);
  };

  return (
    <div>
      <h2 className="text-2xl font-bold mb-4">Buscar Tickets por Tipo</h2>
      <div className="mb-4">
        <label className="block text-sm font-medium text-gray-700">Selecione o tipo</label>
        <select
          value={selectedType}
          onChange={handleTypeChange}
          className="mt-1 block w-64 px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
        >
          <option value="">-- Escolha --</option>
          {types.map((t) => (
            <option key={t} value={t}>{t}</option>
          ))}
        </select>
      </div>

      {loading && <div className="text-center py-8">Carregando...</div>}

      {tickets.length > 0 && (
        <>
          <div className="overflow-x-auto bg-white shadow rounded-lg">
            <table className="min-w-full divide-y divide-gray-200">
              <thead className="bg-gray-50">
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Nome</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Prioridade</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Status</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Data Criação</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-200">
                {tickets.map((ticket) => (
                  <tr key={ticket.id}>
                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{ticket.name}</td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{ticket.priority}</td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{ticket.status}</td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{new Date(ticket.createdAt).toLocaleDateString()}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
          <Pagination page={page} totalPages={totalPages} onPageChange={(p) => fetchTickets(selectedType, p)} />
        </>
      )}
    </div>
  );
};

export default TicketsByType;