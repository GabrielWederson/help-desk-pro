import { useState, useEffect } from 'react';
import { ticketService } from '../../api/ticketService';
import Pagination from '../../components/Pagination';
import toast from 'react-hot-toast';

const CompletedTickets = () => {
  const [tickets, setTickets] = useState([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(true);

  const fetchTickets = async (pageNum = 0) => {
    setLoading(true);
    try {
      const res = await ticketService.admin.getCompleted(pageNum);
      setTickets(res.data.content);
      setTotalPages(res.data.totalPages);
      setPage(res.data.number);
    } catch (err) {
      toast.error('Erro ao carregar tickets completos.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchTickets(0);
  }, []);

  return (
    <div>
      <h2 className="text-2xl font-bold mb-4">Tickets Completos</h2>
      {loading ? (
        <div className="text-center py-8">Carregando...</div>
      ) : (
        <>
          <div className="overflow-x-auto bg-white shadow rounded-lg">
            <table className="min-w-full divide-y divide-gray-200">
              <thead className="bg-gray-50">
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Nome</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Tipo</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Prioridade</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Resolvido por</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Completado em</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-200">
                {tickets.map((ticket) => (
                  <tr key={ticket.id}>
                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{ticket.name}</td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{ticket.type}</td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{ticket.priority}</td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{ticket.resolvedBy}</td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                      {ticket.completedAt ? new Date(ticket.completedAt).toLocaleString() : '-'}
                    </td>
                  </tr>
                ))}
                {tickets.length === 0 && (
                  <tr>
                    <td colSpan="5" className="px-6 py-4 text-center text-sm text-gray-500">
                      Nenhum ticket completo encontrado.
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
          <Pagination page={page} totalPages={totalPages} onPageChange={(p) => fetchTickets(p)} />
        </>
      )}
    </div>
  );
};

export default CompletedTickets;