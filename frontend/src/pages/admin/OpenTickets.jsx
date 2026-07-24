import { useState, useEffect } from 'react';
import { ticketService } from '../../api/ticketService';
import Pagination from '../../components/Pagination';
import Modal from '../../components/Modal';
import toast from 'react-hot-toast';
import { useAuth } from '../../contexts/AuthContext';

const priorityColors = {
  HIGH: 'bg-red-100 text-red-800',
  MEDIUM: 'bg-yellow-100 text-yellow-800',
  LOW: 'bg-green-100 text-green-800',
  COMPLETE: 'bg-gray-100 text-gray-800',
};

const OpenTickets = () => {
  const { user } = useAuth();
  const [tickets, setTickets] = useState([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(true);
  const [modalOpen, setModalOpen] = useState(false);
  const [selectedTicketId, setSelectedTicketId] = useState(null);
  const [completeEmail, setCompleteEmail] = useState('');

  const fetchTickets = async (pageNum = 0) => {
    setLoading(true);
    try {
      const res = await ticketService.admin.getOrderedByPriority(pageNum);

      const normalized = (res.data.content || []).map(ticket => {
        // Tenta encontrar o ID em vários campos possíveis
        const ticketId = ticket.id ?? ticket.ticketId ?? ticket.identifier ?? ticket._id ?? ticket.Id;
        if (ticketId === undefined) {
          console.warn('Ticket sem ID encontrado:', ticket);
        }
        return {
          ...ticket,
          id: ticketId, // sobrescreve id com o valor encontrado
        };
      });
      setTickets(normalized);
      setTotalPages(res.data.totalPages);
      setPage(res.data.number);
    } catch (err) {
      console.error('Erro detalhado:', err.response);
      toast.error(err.response?.data?.detail || 'Erro ao carregar tickets.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchTickets(0);
  }, []);

  const handlePageChange = (newPage) => fetchTickets(newPage);

  const handleMarkInProgress = async (id) => {
    if (!id) {
      toast.error('ID do ticket não encontrado.');
      return;
    }
    try {
      await ticketService.admin.markInProgress(id);
      toast.success('Ticket movido para "Em Andamento".');
      fetchTickets(page);
    } catch (err) {
      console.error('Erro ao marcar em progress:', err.response);
      toast.error(err.response?.data?.detail || 'Erro ao atualizar ticket.');
    }
  };

 const openCompleteModal = (id) => {
     if (!id) {
       toast.error('ID do ticket inválido.');
       return;
     }
     setSelectedTicketId(id);
     setCompleteEmail(user?.email || ''); // preenche com e-mail do admin
     setModalOpen(true);
   };

  const handleComplete = async () => {
    if (!selectedTicketId) {
      toast.error('Nenhum ticket selecionado.');
      return;
    }
    if (!completeEmail) {
      toast.error('Informe o e-mail do responsável.');
      return;
    }
    try {
      await ticketService.admin.completeTicket(selectedTicketId, completeEmail);
      toast.success('Ticket completado com sucesso.');
      setModalOpen(false);
      fetchTickets(page);
    } catch (err) {
      console.error('Erro ao completar ticket:', err.response);
      toast.error(err.response?.data?.detail || 'Erro ao completar ticket.');
    }
  };

  return (
    <div>
      <h2 className="text-2xl font-bold mb-4">Tickets Abertos (ordenados por prioridade)</h2>
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
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Status</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Data Criação</th>
                  <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase">Ações</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-200">
                {tickets.map((ticket) => (
                  <tr key={ticket.id} className="hover:bg-gray-50">
                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                      {ticket.name}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{ticket.type}</td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <span className={`px-2 py-1 text-xs rounded-full ${priorityColors[ticket.priority] || 'bg-gray-100'}`}>
                        {ticket.priority}
                      </span>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{ticket.status}</td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                      {new Date(ticket.createdAt).toLocaleDateString()}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                      {ticket.status === 'CREATED' && (
                        <button
                          onClick={() => handleMarkInProgress(ticket.id)}
                          className="text-blue-600 hover:text-blue-900 mr-2"
                        >
                          Mover para Andamento
                        </button>
                      )}
                      {ticket.status === 'IN_PROGRESS' && (
                        <button
                          onClick={() => openCompleteModal(ticket.id)}
                          className="text-green-600 hover:text-green-900"
                        >
                          Completar
                        </button>
                      )}
                    </td>
                  </tr>
                ))}
                {tickets.length === 0 && (
                  <tr>
                    <td colSpan="6" className="px-6 py-4 text-center text-sm text-gray-500">
                      Nenhum ticket aberto encontrado.
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
          <Pagination page={page} totalPages={totalPages} onPageChange={handlePageChange} />
        </>
      )}

      <Modal isOpen={modalOpen} onClose={() => setModalOpen(false)}>
              <h3 className="text-lg font-medium mb-4">Completar Ticket</h3>
              <div className="mb-4">
                <label className="block text-sm font-medium text-gray-700">E-mail do responsável (admin)</label>
                <input
                  type="email"
                  value={completeEmail}
                  readOnly
                  className="mt-1 block w-full px-3 py-2 bg-gray-100 border border-gray-300 rounded-md shadow-sm text-gray-700 cursor-not-allowed"
                />
                <p className="text-xs text-gray-500 mt-1">O ticket será resolvido com o e‑mail do administrador logado.</p>
              </div>
              <div className="flex justify-end gap-2">
                <button
                  onClick={() => setModalOpen(false)}
                  className="px-4 py-2 text-sm border rounded-md hover:bg-gray-50"
                >
                  Cancelar
                </button>
                <button
                  onClick={handleComplete}
                  className="px-4 py-2 text-sm bg-blue-600 text-white rounded-md hover:bg-blue-700"
                >
                  Confirmar
                </button>
              </div>
            </Modal>
          </div>
        );
      };

export default OpenTickets;