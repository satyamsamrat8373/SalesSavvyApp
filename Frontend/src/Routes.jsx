import { Routes, Route } from 'react-router-dom';
import Login from './Login';
import RegistrationPage from './RegistrationPage';
import Customer from './Customer';
import CartPage from './CartPage';
import OrdersPage from './OrderPage';
import ProfilePage from './ProfilePage';
import AdminLogin from './AdminLogin';
import AdminDashboard from './AdminDashboard';

export function AppRoutes() {
  return (
    <Routes>
      <Route path="/" element={<Login />} />
      <Route path="/register" element={<RegistrationPage />} />
      <Route path="/customer" element={<Customer />} />
      <Route path="/UserCartPage" element={<CartPage />} />
      <Route path="/orders" element={<OrdersPage />} />
      <Route path="/profile" element={<ProfilePage />} />
      <Route path="/admin" element={<AdminLogin />} />
      <Route path="/admindashboard" element={<AdminDashboard />} />
    </Routes>
  );
}