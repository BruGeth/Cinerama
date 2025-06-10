import Navbar from "../components/Navbar"
import Footer from "../components/Footer"

function MainLayout({ children }) {
  return (
    <div className="main-layout">
      <Navbar />
      <main className="main-content">{children}</main>
      <Footer />
    </div>
  )
}

export default MainLayout
