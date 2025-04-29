import "./Footer.css";

const Footer = () => {
  return (
    <footer className="footer">
      <p>© {new Date().getFullYear()} Cinerama. All rights reserved.</p>
    </footer>
  );
};

export default Footer;
