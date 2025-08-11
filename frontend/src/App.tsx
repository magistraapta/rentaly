import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import MainLayout from './layouts/MainLayout';
import AdminLayout from './layouts/AdminLayout';
import HomePage from './pages/user/HomePage';
import CarsPage from './pages/user/CarsPage';
import CarDetailsPage from './pages/user/CarDetailsPage';
import BookingsPage from './pages/user/BookingsPage';
import Dashboard from './pages/admin/Dashboard';
import AuthPage from './pages/AuthPage';
import AdminAuthPage from './pages/admin/AdminAuthPage';
import NotFoundPage from './pages/NotFoundPage';
import ProtectedAdminRoute from './components/ProtectedAdminRoute';
import './App.css'

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/auth" element={<AuthPage />} />
        
        {/* User Routes */}
        <Route path="/" element={<MainLayout />}>
          <Route index element={<HomePage />} />
          <Route path="cars" element={<CarsPage />} />
          <Route path="cars/:id" element={<CarDetailsPage />} />
          <Route path="bookings" element={<BookingsPage />} />
        </Route>
        
        {/* Admin Auth Route */}
        <Route path="/admin/login" element={<AdminAuthPage />} />
        
        {/* Protected Admin Routes */}
        <Route path="/admin" element={
          <ProtectedAdminRoute>
            <AdminLayout />
          </ProtectedAdminRoute>
        }>
          <Route index element={<Dashboard />} />
          <Route path="cars" element={<Dashboard />} />
          <Route path="bookings" element={<Dashboard />} />
          <Route path="users" element={<Dashboard />} />
          <Route path="settings" element={<Dashboard />} />
        </Route>
        
        <Route path="*" element={<NotFoundPage />} />
      </Routes>
    </Router>
  )
}

export default App
