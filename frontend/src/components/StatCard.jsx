export default function StatCard({ title, value, percentage }) {
  return (
    <div className="bg-red-200 p-6 rounded-lg shadow">
      <h3 className="text-lg font-semibold mb-2">{title}</h3>
      <p className="text-gray-500 text-2xl">{value}</p>

      {percentage !== undefined && (
        <>
          <p className="text-gray-800 text-sm mt-1">
            ({percentage}% verwendet)
          </p>
          <div className="w-full bg-gray-300 rounded-full h-4 mt-2">
            <div
              className="bg-blue-500 h-4 rounded-full"
              style={{ width: `${percentage}%` }}
            ></div>
          </div>
        </>
      )}
    </div>
  );
}
