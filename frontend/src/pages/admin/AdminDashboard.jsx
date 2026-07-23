import { NavLink, Outlet } from 'react-router-dom';
import Navbar from '../../components/Navbar';

const AdminDashboard = () => {
  const linkClass = ({ isActive }) =>
    `block px-4 py-2 rounded-md text-sm ${
      isActive ? 'bg-blue-50 text-blue-700' : 'text-gray-700 hover:bg-gray-100'
    }`;

  return (
    <div className="min-h-screen bg-gray-50">
      <Navbar />
      <div className="flex">
        <aside className="w-64 bg-white shadow-sm min-h-screen p-4">
          <nav className="space-y-2">
            <NavLink to="/admin/open-tickets" className={linkClass}>
              Tickets Abertos (Prioridade)
            </NavLink>
            <NavLink to="/admin/completed-tickets" className={linkClass}>
              Tickets Completos
            </NavLink>
            <NavLink to="/admin/tickets-by-type" className={linkClass}>
              Buscar por Tipo
            </NavLink>
            <NavLink to="/admin/tickets-by-priority" className={linkClass}>
              Buscar por Prioridade
            </NavLink>
            <NavLink to="/admin/ticket-by-id" className={linkClass}>
              Buscar por ID
            </NavLink>
          </nav>
        </aside>
        <main className="flex-1 p-6">
          <Outlet />
        </main>
      </div>
    </div>
  );
};

export default AdminDashboard;