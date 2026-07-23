const Pagination = ({ page, totalPages, onPageChange }) => {
  if (totalPages <= 1) return null;

  const pages = [];
  for (let i = 0; i < totalPages; i++) {
    pages.push(i);
  }

  return (
    <nav className="flex justify-center mt-4">
      <ul className="flex gap-1">
        <li>
          <button
            onClick={() => onPageChange(page - 1)}
            disabled={page === 0}
            className="px-3 py-1 rounded border disabled:opacity-50 hover:bg-gray-100"
          >
            Anterior
          </button>
        </li>
        {pages.map(p => (
          <li key={p}>
            <button
              onClick={() => onPageChange(p)}
              className={`px-3 py-1 rounded border ${
                p === page ? 'bg-blue-500 text-white' : 'hover:bg-gray-100'
              }`}
            >
              {p + 1}
            </button>
          </li>
        ))}
        <li>
          <button
            onClick={() => onPageChange(page + 1)}
            disabled={page === totalPages - 1}
            className="px-3 py-1 rounded border disabled:opacity-50 hover:bg-gray-100"
          >
            Próximo
          </button>
        </li>
      </ul>
    </nav>
  );
};

export default Pagination;