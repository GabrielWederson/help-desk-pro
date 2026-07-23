import api from './config';

export const authService = {
  signin: (email, password) =>
    api.post('/auth/signin', { email, password }),

  register: (email, name, password) =>
    api.post('/auth/register', { email, name, password }),
};