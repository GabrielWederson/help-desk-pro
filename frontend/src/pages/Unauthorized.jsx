const Unauthorized = () => {
  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-100">
      <div className="text-center">
        <h1 className="text-4xl font-bold text-red-500 mb-4">403</h1>
        <p className="text-xl text-gray-700">Acesso não autorizado</p>
        <p className="text-gray-500 mt-2">Você não tem permissão para acessar esta página.</p>
      </div>
    </div>
  );
};

export default Unauthorized;