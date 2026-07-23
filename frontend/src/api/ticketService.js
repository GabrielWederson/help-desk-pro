import api from './config';


const adminAPI = {
  getAll: (page = 0, size = 10) =>
    api.get(`/api/ticket/v1?page=${page}&size=${size}&sort=createdAt,desc`),

  getById: (id) => api.get(`/api/ticket/v1/${id}`),

  getByPriority: (priority, page = 0, size = 10) =>
    api.get(`/api/ticket/v1/priority/${priority}?page=${page}&size=${size}`),

  getByType: (type, page = 0, size = 10) =>
    api.get(`/api/ticket/v1/type/${type}?page=${page}&size=${size}`),

  getOrderedByPriority: (page = 0, size = 10) =>
    api.get(`/api/ticket/v1/orderByAllPriorities?page=${page}&size=${size}`),

  getCompleted: (page = 0, size = 10) =>
    api.get(`/api/ticket/v1/complete?page=${page}&size=${size}`),

  markInProgress: (id) => api.patch(`/api/ticket/v1/${id}`, {}),

  completeTicket: (id, email) =>
    api.patch('/api/ticket/v1/complete', { id, email }),
};


const userAPI = {
  create: (ticketData) => api.post('/api/ticket/v1', ticketData),

  update: (ticketData) => api.put('/api/ticket/v1', ticketData),

  delete: (id) => api.delete(`/api/ticket/v1/${id}`),
};

export const ticketService = {
  admin: adminAPI,
  user: userAPI,
};