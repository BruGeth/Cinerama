const API_BASE_URL = "/api/auth";

const passwordResetService = {
  sendResetEmail: async (email) => {
    const res = await fetch(`${API_BASE_URL}/forgot-password`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email }),
    });
    if (!res.ok) throw new Error(await res.text());
    return await res.text();
  },

  validateResetToken: async (email, token) => {
    const res = await fetch(`${API_BASE_URL}/validate-reset-token`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email, token }),
    });
    if (!res.ok) throw new Error(await res.text());
    return await res.text();
  },

  changePassword: async (email, newPassword, confirmPassword) => {
    const res = await fetch(`${API_BASE_URL}/change-password`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email, newPassword, confirmPassword }),
    });
    if (!res.ok) throw new Error(await res.text());
    return await res.text();
  }
};

export default passwordResetService;