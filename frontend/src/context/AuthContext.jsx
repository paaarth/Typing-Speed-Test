import { createContext, useContext, useState, useCallback } from 'react';
import { loginUser, registerUser } from '../api/authApi.js';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [username, setUsername] = useState(() => localStorage.getItem('username'));
  const [token, setToken] = useState(() => localStorage.getItem('token'));
  const [role, setRole] = useState(() => localStorage.getItem('role'));

  const persist = (data) => {
    localStorage.setItem('token', data.token);
    localStorage.setItem('username', data.username);
    localStorage.setItem('role', data.role);
    setToken(data.token);
    setUsername(data.username);
    setRole(data.role);
  };

  const login = useCallback(async (usernameInput, password) => {
    const data = await loginUser(usernameInput, password);
    persist(data);
  }, []);

  const register = useCallback(async (usernameInput, password, email) => {
    const data = await registerUser(usernameInput, password, email);
    persist(data);
  }, []);

  const logout = useCallback(() => {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    localStorage.removeItem('role');
    setToken(null);
    setUsername(null);
    setRole(null);
  }, []);

  const value = {
    username,
    token,
    role,
    isAuthenticated: Boolean(token),
    isAdmin: role === 'ADMIN',
    login,
    register,
    logout,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth must be used within an AuthProvider');
  return ctx;
}
