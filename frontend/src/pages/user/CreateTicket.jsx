import { useState } from 'react';
import { ticketService } from '../../api/ticketService';
import toast from 'react-hot-toast';

const types = ['INCIDENT', 'PROBLEM', 'CHANGE_REQUEST', 'SERVICE_REQUEST'];
const priorities = ['HIGH', 'MEDIUM', 'LOW'];

const CreateTicket = () => {
  const [form, setForm] = useState({
    name: '',
    description: '',
    type: 'INCIDENT',
    priority: 'MEDIUM',
  });
  const [loading, setLoading] = useState(false);
  const [createdTicket, setCreatedTicket] = useState(null);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!form.name || !form.description) {
      toast.error('Nome e descrição são obrigatórios.');
      return;
    }

    setLoading(true);
    try {
      const res = await ticketService.user.create(form);
      setCreatedTicket(res.data);
      toast.success('Ticket criado com sucesso!');
      // Limpar formulário?
      setForm({ name: '', description: '', type: 'INCIDENT', priority: 'MEDIUM' });
    } catch (err) {
      toast.error(err.response?.data?.message || 'Erro ao criar ticket.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <h3 className="text-xl font-bold mb-4">Novo Ticket</h3>
      <form onSubmit={handleSubmit} className="bg-white shadow rounded-lg p-6 space-y-4">
        <div>
          <label className="block text-sm font-medium text-gray-700">Nome</label>
          <input
            type="text"
            name="name"
            required
            value={form.name}
            onChange={handleChange}
            className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
          />
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700">Descrição</label>
          <textarea
            name="description"
            required
            rows="3"
            value={form.description}
            onChange={handleChange}
            className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
          />
        </div>
        <div className="grid grid-cols-2 gap-4">
          <div>
            <label className="block text-sm font-medium text-gray-700">Tipo</label>
            <select
              name="type"
              value={form.type}
              onChange={handleChange}
              className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
            >
              {types.map(t => <option key={t} value={t}>{t}</option>)}
            </select>
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700">Prioridade</label>
            <select
              name="priority"
              value={form.priority}
              onChange={handleChange}
              className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
            >
              {priorities.map(p => <option key={p} value={p}>{p}</option>)}
            </select>
          </div>
        </div>
        <button
          type="submit"
          disabled={loading}
          className="w-full py-2 px-4 bg-blue-600 text-white rounded-md hover:bg-blue-700 disabled:opacity-50"
        >
          {loading ? 'Criando...' : 'Criar Ticket'}
        </button>
      </form>

      {createdTicket && (
        <div className="mt-4 p-4 bg-green-50 border border-green-200 rounded-md">
          <p className="text-green-800">Ticket criado com sucesso!</p>
          <p className="text-sm">ID: <strong>{createdTicket.id}</strong></p>
          <p className="text-sm">Guarde este ID para gerenciar o ticket posteriormente.</p>
        </div>
      )}
    </div>
  );
};

export default CreateTicket;