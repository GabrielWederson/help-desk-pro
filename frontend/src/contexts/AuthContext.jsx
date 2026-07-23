import { createContext, useContext, useState, useEffect } from 'react';

// Função auxiliar para decodificar JWT
function parseJwt(token) {
  try {
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(
      atob(base64)
        .split('')
        .map(c => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
        .join('')
    );
    return JSON.parse(jsonPayload);
  } catch (e) {
    return null;
  }
}

// Converte roles como "USER" para "ROLE_USER" e mantém "ROLE_ADMIN"
function normalizeRole(role) {
  if (!role) return null;
  // Se já contém "ROLE_", retorna como está
  if (role.startsWith('ROLE_')) return role;
  // Caso contrário, adiciona o prefixo
  return `ROLE_${role}`;
}

// Extrai usuário (email + role) do token, tratando vários formatos do Spring Security
export function getUserFromToken(token) {
  const decoded = parseJwt(token);
  if (!decoded) return null;

  let role = null;

  // Tenta obter role de diferentes campos comuns no Spring Boot
  if (decoded.role) {
    role = decoded.role;
  } else if (decoded.roles && Array.isArray(decoded.roles) && decoded.roles.length > 0) {
    role = decoded.roles[0]; // ex: ["USER"] ou ["ROLE_USER"]
  } else if (decoded.authorities && Array.isArray(decoded.authorities) && decoded.authorities.length > 0) {
    const first = decoded.authorities[0];
    role = typeof first === 'string' ? first : first.authority;
  }

  return {
    email: decoded.sub,
    role: normalizeRole(role), // Normaliza para "ROLE_USER" ou "ROLE_ADMIN"
  };
}

const AuthContext = createContext(null);

export const useAuth = () => useContext(AuthContext);

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const accessToken = localStorage.getItem('accessToken');
    if (accessToken) {
      const userData = getUserFromToken(accessToken);
      if (userData && userData.role && userData.email) {
        setUser(userData);
      } else {
        localStorage.clear();
      }
    }
    setLoading(false);
  }, []);

  const login = (accessToken, refreshToken) => {
    localStorage.setItem('accessToken', accessToken);
    localStorage.setItem('refreshToken', refreshToken);
    const userData = getUserFromToken(accessToken);
    setUser(userData);
  };

  const logout = () => {
    localStorage.clear();
    setUser(null);
  };

  const value = {
    user,
    login,
    logout,
    loading,
    isAuthenticated: !!user && !!user.role,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};