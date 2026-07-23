import { useState } from 'react';
import { ticketService } from '../../api/ticketService';
import toast from 'react-hot-toast';

const types = ['INCIDENT', 'PROBLEM', 'CHANGE_REQUEST', 'SERVICE_REQUEST'];
const priorities = ['HIGH', 'MEDIUM', 'LOW'];

const ManageTicket = () => {
  const [ticketId, setTicketId] = useState('');
  const [mode, setMode] = useState(null); // 'edit' | 'delete'
  const [form, setForm] = useState({
    name: '',
    description: '',
    type: 'INCIDENT',
    priority: 'MEDIUM',
  });
  const [loading, setLoading] = useState(false);

  const handleIdSubmit = (action) => {
    if (!ticketId) {
      toast.error('Informe o ID do ticket.');
      return;
    }
    setMode(action);
    if (action === 'edit') {
      // Não temos endpoint para buscar por ID para usuário, então o usuário precisa preencher tudo
      // Preenchemos o formulário com valores vazios, mas o ID fica fixo.
      setForm({ name: '', description: '', type: 'INCIDENT', priority: 'MEDIUM' });
    }
  };

  const handleEditSubmit = async (e) => {
    e.preventDefault();
    if (!form.name || !form.description) {
      toast.error('Preencha todos os campos obrigatórios.');
      return;
    }
    setLoading(true);
    try {
      const payload = {
        id: Number(ticketId),
        ...form,
      };
      await ticketService.user.update(payload);
      toast.success('Ticket atualizado com sucesso!');
      setMode(null);
      setTicketId('');
    } catch (err) {
      toast.error(err.response?.data?.message || 'Erro ao atualizar ticket.');
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async () => {
    if (!window.confirm('Tem certeza que deseja excluir este ticket?')) return;
    setLoading(true);
    try {
      await ticketService.user.delete(ticketId);
      toast.success('Ticket excluído com sucesso.');
      setMode(null);
      setTicketId('');
    } catch (err) {
      toast.error(err.response?.data?.message || 'Erro ao excluir ticket.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <h3 className="text-xl font-bold mb-4">Gerenciar Ticket (por ID)</h3>

      {!mode && (
        <div className="bg-white shadow rounded-lg p-6 space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700">ID do Ticket</label>
            <input
              type="number"
              value={ticketId}
              onChange={(e) => setTicketId(e.target.value)}
              className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
            />
          </div>
          <div className="flex gap-2">
            <button
              onClick={() => handleIdSubmit('edit')}
              className="px-4 py-2 bg-yellow-500 text-white rounded-md hover:bg-yellow-600"
            >
              Editar
            </button>
            <button
              onClick={() => handleIdSubmit('delete')}
              className="px-4 py-2 bg-red-600 text-white rounded-md hover:bg-red-700"
            >
              Excluir
            </button>
          </div>
        </div>
      )}

      {mode === 'edit' && (
        <div className="bg-white shadow rounded-lg p-6">
          <h4 className="text-lg font-medium mb-4">Editando Ticket #{ticketId}</h4>
          <form onSubmit={handleEditSubmit} className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700">Nome</label>
              <input
                type="text"
                required
                value={form.name}
                onChange={(e) => setForm({ ...form, name: e.target.value })}
                className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700">Descrição</label>
              <textarea
                required
                rows="3"
                value={form.description}
                onChange={(e) => setForm({ ...form, description: e.target.value })}
                className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
              />
            </div>
            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700">Tipo</label>
                <select
                  value={form.type}
                  onChange={(e) => setForm({ ...form, type: e.target.value })}
                  className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                >
                  {types.map(t => <option key={t} value={t}>{t}</option>)}
                </select>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">Prioridade</label>
                <select
                  value={form.priority}
                  onChange={(e) => setForm({ ...form, priority: e.target.value })}
                  className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                >
                  {priorities.map(p => <option key={p} value={p}>{p}</option>)}
                </select>
              </div>
            </div>
            <div className="flex gap-2">
              <button
                type="submit"
                disabled={loading}
                className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 disabled:opacity-50"
              >
                {loading ? 'Salvando...' : 'Salvar'}
              </button>
              <button
                type="button"
                onClick={() => setMode(null)}
                className="px-4 py-2 border rounded-md hover:bg-gray-50"
              >
                Cancelar
              </button>
            </div>
          </form>
        </div>
      )}

      {mode === 'delete' && (
        <div className="bg-white shadow rounded-lg p-6 text-center">
          <p className="mb-4">Confirmar exclusão do ticket #{ticketId}?</p>
          <div className="flex justify-center gap-2">
            <button
              onClick={handleDelete}
              disabled={loading}
              className="px-4 py-2 bg-red-600 text-white rounded-md hover:bg-red-700 disabled:opacity-50"
            >
              {loading ? 'Excluindo...' : 'Sim, excluir'}
            </button>
            <button
              onClick={() => setMode(null)}
              className="px-4 py-2 border rounded-md hover:bg-gray-50"
            >
              Cancelar
            </button>
          </div>
        </div>
      )}
    </div>
  );
};

export default ManageTicket;