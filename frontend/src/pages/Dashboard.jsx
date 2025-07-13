import { useState, useEffect } from "react";
import Sidebar from "../components/Sidebar";
import StatCard from "../components/StatCard";

export default function Dashboard() {
  const [dashboardData, setDashboardData] = useState(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    document.title = "Dashboard";

    const fetchData = async () => {
      try {
        const res = await fetch("/api/dashboard", {
          credentials: "include",
        });

        if (!res.ok) {
          throw new Error("Not autoriesed");
        }

        const data = await res.json();
        setDashboardData(data);
      } catch (err) {
        console.error(err);
        setError("Error while loading Dashboard");
      }
    };

    fetchData();
  }, []);

  if (error) return <div>{error}</div>;
  if (!dashboardData) return <div>Loading...</div>;

  return (
    <div className="bg-gray-300 min-h-screen flex">
      {/* Sidebar */}
      <Sidebar />
      <main className="flex-1 p-6 overflow-y-auto">
        <h1 className="text-4xl font-bold mb-4">Willkommen im Dashboard</h1>

        {/* Statcards */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          <StatCard
            title="Dateien gespeichert"
            value={dashboardData.fileCount}
          />
          <StatCard title="Benutzer angemeldet (not implemented)" value="8" />
          <StatCard
            title="Speicherplatz genutzt"
            value={`${dashboardData.usedSpace} / ${dashboardData.totalSpace}`}
            percentage={dashboardData.usagePercent}
          />
        </div>
      </main>
    </div>
  );
}
