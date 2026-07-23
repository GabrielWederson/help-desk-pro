import { useState } from 'react';
import { ticketService } from '../../api/ticketService';
import toast from 'react-hot-toast';

const TicketById = () => {
  const [ticketId, setTicketId] = useState('');
  const [ticket, setTicket] = useState(null);
  const [loading, setLoading] = useState(false);

  const handleSearch = async (e) => {
    e.preventDefault();
    if (!ticketId) return;
    setLoading(true);
    setTicket(null);
    try {
      const res = await ticketService.admin.getById(ticketId);
      setTicket(res.data);
    } catch (err) {
      if (err.response?.status === 404) {
        toast.error('Ticket não encontrado.');
      } else {
        toast.error('Erro ao buscar ticket.');
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <h2 className="text-2xl font-bold mb-4">Buscar Ticket por ID</h2>
      <form onSubmit={handleSearch} className="flex gap-2 mb-6">
        <input
          type="number"
          placeholder="ID do ticket"
          value={ticketId}
          onChange={(e) => setTicketId(e.target.value)}
          className="flex-1 px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
        />
        <button
          type="submit"
          disabled={loading}
          className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 disabled:opacity-50"
        >
          Buscar
        </button>
      </form>

      {loading && <div className="text-center py-8">Carregando...</div>}

      {ticket && (
        <div className="bg-white shadow rounded-lg p-6">
          <h3 className="text-lg font-medium mb-2">Detalhes do Ticket #{ticket.id}</h3>
          <dl className="grid grid-cols-2 gap-4">
            <div>
              <dt className="text-sm font-medium text-gray-500">Nome</dt>
              <dd className="text-sm text-gray-900">{ticket.name}</dd>
            </div>
            <div>
              <dt className="text-sm font-medium text-gray-500">Tipo</dt>
              <dd className="text-sm text-gray-900">{ticket.type}</dd>
            </div>
            <div>
              <dt className="text-sm font-medium text-gray-500">Prioridade</dt>
              <dd className="text-sm text-gray-900">{ticket.priority}</dd>
            </div>
            <div>
              <dt className="text-sm font-medium text-gray-500">Status</dt>
              <dd className="text-sm text-gray-900">{ticket.status}</dd>
            </div>
            <div>
              <dt className="text-sm font-medium text-gray-500">Descrição</dt>
              <dd className="text-sm text-gray-900">{ticket.description}</dd>
            </div>
            <div>
              <dt className="text-sm font-medium text-gray-500">Criado em</dt>
              <dd className="text-sm text-gray-900">{new Date(ticket.createdAt).toLocaleString()}</dd>
            </div>
            {ticket.resolvedBy && (
              <div>
                <dt className="text-sm font-medium text-gray-500">Resolvido por</dt>
                <dd className="text-sm text-gray-900">{ticket.resolvedBy}</dd>
              </div>
            )}
            {ticket.completedAt && (
              <div>
                <dt className="text-sm font-medium text-gray-500">Completado em</dt>
                <dd className="text-sm text-gray-900">{new Date(ticket.completedAt).toLocaleString()}</dd>
              </div>
            )}
          </dl>
        </div>
      )}
    </div>
  );
};

export default TicketById;