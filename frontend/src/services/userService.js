const userService = {
    registerUser: (data) => {
      console.log("Registering user:", data);
      // Example:
      // return fetch("/api/register", {
      //   method: "POST",
      //   headers: { "Content-Type": "application/json" },
      //   body: JSON.stringify(data),
      // }).then((res) => res.json());
    },
  
    loginUser: (credentials) => {
      console.log("Logging in user:", credentials);
      // Example:
      // return fetch("/api/login", {
      //   method: "POST",
      //   headers: { "Content-Type": "application/json" },
      //   body: JSON.stringify(credentials),
      // }).then((res) => res.json());
    },
  };
  
  export default userService;
  