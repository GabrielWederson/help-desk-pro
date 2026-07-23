import { Routes, Route, Navigate } from 'react-router-dom';
import { useAuth } from './contexts/AuthContext';
import ProtectedRoute from './components/ProtectedRoute';
import Login from './pages/Login';
import Register from './pages/Register';
import Unauthorized from './pages/Unauthorized';
import NotFound from './pages/NotFound';
import AdminDashboard from './pages/admin/AdminDashboard';
import OpenTickets from './pages/admin/OpenTickets';
import CompletedTickets from './pages/admin/CompletedTickets';
import TicketsByType from './pages/admin/TicketsByType';
import TicketsByPriority from './pages/admin/TicketsByPriority';
import TicketById from './pages/admin/TicketById';
import UserDashboard from './pages/user/UserDashboard';

function App() {
  const { user, loading } = useAuth();

  if (loading) {
    return (
      <div className="flex justify-center items-center h-screen">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-500"></div>
      </div>
    );
  }

  return (
    <Routes>
      {/* Rotas públicas */}
      <Route path="/login" element={user ? <Navigate to={user.role === 'ROLE_ADMIN' ? '/admin' : '/user'} replace /> : <Login />} />
      <Route path="/register" element={user ? <Navigate to={user.role === 'ROLE_ADMIN' ? '/admin' : '/user'} replace /> : <Register />} />
      <Route path="/unauthorized" element={<Unauthorized />} />

      {/* Rotas ADMIN */}
      <Route
        path="/admin"
        element={
          <ProtectedRoute allowedRoles={['ROLE_ADMIN']}>
            <AdminDashboard />
          </ProtectedRoute>
        }
      >
        <Route index element={<Navigate to="open-tickets" replace />} />
        <Route path="open-tickets" element={<OpenTickets />} />
        <Route path="completed-tickets" element={<CompletedTickets />} />
        <Route path="tickets-by-type" element={<TicketsByType />} />
        <Route path="tickets-by-priority" element={<TicketsByPriority />} />
        <Route path="ticket-by-id" element={<TicketById />} />
      </Route>

      {/* Rota USER */}
      <Route
        path="/user"
        element={
          <ProtectedRoute allowedRoles={['ROLE_USER']}>
            <UserDashboard />
          </ProtectedRoute>
        }
      />

      {/* Redirecionamento padrão e 404 */}
      <Route path="/" element={<Navigate to={user ? (user.role === 'ROLE_ADMIN' ? '/admin' : '/user') : '/login'} replace />} />
      <Route path="*" element={<NotFound />} />
    </Routes>
  );
}

export default App;