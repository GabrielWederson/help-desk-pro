import Navbar from '../../components/Navbar';
import CreateTicket from './CreateTicket';
import ManageTicket from './ManageTicket';
import { useState } from 'react';

const UserDashboard = () => {
  const [activeTab, setActiveTab] = useState('create'); // 'create' | 'manage'

  return (
    <div className="min-h-screen bg-gray-50">
      <Navbar />
      <div className="max-w-4xl mx-auto p-6">
        <div className="flex gap-4 mb-6">
          <button
            onClick={() => setActiveTab('create')}
            className={`px-4 py-2 rounded-md text-sm font-medium ${
              activeTab === 'create' ? 'bg-blue-600 text-white' : 'bg-white text-gray-700 border hover:bg-gray-100'
            }`}
          >
            Novo Ticket
          </button>
          <button
            onClick={() => setActiveTab('manage')}
            className={`px-4 py-2 rounded-md text-sm font-medium ${
              activeTab === 'manage' ? 'bg-blue-600 text-white' : 'bg-white text-gray-700 border hover:bg-gray-100'
            }`}
          >
            Gerenciar Ticket (por ID)
          </button>
        </div>

        {activeTab === 'create' ? <CreateTicket /> : <ManageTicket />}
      </div>
    </div>
  );
};

export default UserDashboard;