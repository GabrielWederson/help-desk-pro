const Modal = ({ isOpen, onClose, children }) => {
  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 z-50 overflow-y-auto">
      <div className="flex items-center justify-center min-h-screen px-4">
        <div className="fixed inset-0 bg-black opacity-40" onClick={onClose}></div>
        <div className="bg-white rounded-lg shadow-xl z-50 max-w-md w-full p-6">
          {children}
        </div>
      </div>
    </div>
  );
};

export default Modal;