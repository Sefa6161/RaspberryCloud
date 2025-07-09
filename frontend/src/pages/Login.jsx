import { useState } from "react";

export default function Login() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [logoutSuccess, setLogoutSuccess] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();

    const formData = new URLSearchParams();
    formData.append("username", username);
    formData.append("password", password);

    try {
      const res = await fetch("/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/x-www-form-urlencoded",
        },
        // body: formData,
        body: new URLSearchParams({
          username,
          password,
        }),
        credentials: "include",
      });

      if (res.ok) {
        window.location.href = "/dashboard";
      } else {
        setError("Login failed, try again");
      }
    } catch (error) {
      console.error(error);
      setError("There is a Error");
    }
  };

  return (
    <div className="bg-gray-300 flex justify-center pt-24 min-h-screen">
      <div className="bg-gray-200 p-8 rounded-2xl shadow-md w-full max-w-sm">
        <h1 className="text-2xl font-bold mb-4 text-center">Login</h1>

        {logoutSuccess && (
          <p className="text-green-600 mb-2">
            Du hast dich erfolgreich ausgeloggt.
          </p>
        )}
        {error && <p className="text-red-600 mb-2">{error}</p>}

        <form onSubmit={handleSubmit} className="flex flex-col gap-4">
          <input
            type="text"
            name="username"
            placeholder="Benutzername"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            className="bg-gray-200 w-full border-0 border-b-2 border-gray-400 focus:outline-none focus:border-blue-500 transition-colors duration-200"
          />
          <input
            type="password"
            name="password"
            placeholder="Passwort"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            className="bg-gray-200 w-full border-0 border-b-2 border-gray-400 focus:outline-none focus:border-blue-500 transition-colors duration-200"
          />
          <button
            type="submit"
            className="bg-blue-600 text-white py-2 rounded hover:bg-blue-700"
          >
            Login
          </button>
        </form>

        <div className="mt-2 text-center flex flex-col">
          <a href="/signup" className="text-blue-600 hover:underline">
            Registrieren
          </a>
          <a href="/change_password" className="text-gray-400 hover:underline">
            Passwort vergessen (not implemented)
          </a>
        </div>
      </div>
    </div>
  );
}
