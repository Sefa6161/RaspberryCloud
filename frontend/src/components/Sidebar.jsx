import { useState } from "react";

export default function Sidebar() {
  const [isOpen, setIsOpen] = useState(true);

  const toggleSidebar = () => {
    setIsOpen(!isOpen);
  };

  return (
    <div>
      {/* Toggle button */}
      <button
        onClick={toggleSidebar}
        className="absolute top-4 left-4 z-50 text-gray-600 md:hidden"
      >
        ☰ Menü
      </button>
      {/* Toggle button */}

      <aside
        className={`bg-gray-200 h-screen w-64 p-6 shadow-md transition-all duration-300 ease-in-out overflow-hidden
    ${isOpen ? "translate-x-0" : "-translate-x-full"} 
    md:translate-x-0
  `}
      >
        <h2 className="text-2xl font-bold mb-8">NAS Dashboard</h2>
        <nav className="flex flex-col gap-4">
          <a href="/dashboard" className="text-gray-700 hover:text-blue-500">
            Übersicht
          </a>
          <a href="/files" className="text-gray-700 hover:text-blue-500">
            Meine Dateien
          </a>
          <a href="/users" className="text-gray-700 hover:text-blue-500">
            Benutzer (not implemented)
          </a>
          <a href="/settings" className="text-gray-700 hover:text-blue-500">
            Einstellungen (not implemented)
          </a>
          <form action="/logout" method="post">
            <button
              type="submit"
              className="text-red-500 hover:text-red-700 text-left w-full"
            >
              Logout
            </button>
          </form>
        </nav>
      </aside>
    </div>
  );
}
