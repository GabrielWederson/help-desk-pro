import { useAuth } from '../contexts/AuthContext';
import { useNavigate } from 'react-router-dom';

const Navbar = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <nav className="bg-white shadow-sm border-b border-gray-200">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 flex justify-between h-16 items-center">
        <div className="flex items-center">
          <h1 className="text-xl font-semibold text-gray-800">Help Desk Pro</h1>
        </div>
        <div className="flex items-center gap-4">
          <span className="text-sm text-gray-600">
            {user?.email} ({user?.role === 'ROLE_ADMIN' ? 'Admin' : 'Usuário'})
          </span>
          <button
            onClick={handleLogout}
            className="px-3 py-1.5 text-sm rounded-md bg-red-50 text-red-600 hover:bg-red-100 transition-colors"
          >
            Sair
          </button>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;