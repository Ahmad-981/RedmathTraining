import React from 'react';
import { Routes, Route, useLocation } from 'react-router-dom';
import Home from './pages/Auth/Home/Home';
import Login from './pages/Auth/login';
import SignUp from './pages/Auth/signup';
import Dashboard from './pages/dashboard/Dashboard';
import CreateAccounts from './pages/dashboard/accounts';
import ViewTransactions from './pages/dashboard/viewTransactions';
import LandingPage from './component/landingpage/LandingPage';
import AdminDashboard from './component/admin/AdminDashboard.js';
import ViewAccounts from './component/admin/viewAccounts/ViewAccounts';

function App() {
  const location = useLocation();

  return (
    <Routes>
      {location.pathname === '/' ? (
        <Route path="/" element={<LandingPage />} />
      ) : (
        <>
          <Route path="/home" element={<Home />} />
          <Route path="/login" element={<Login />} />
          <Route path="/signup" element={<SignUp />} />
          <Route path="/dashboard" element={<Dashboard />}>
            <Route path="createAccounts" element={<CreateAccounts />} />
            <Route path="viewTransactions" element={<ViewTransactions />} />
          </Route>
          <Route path="/admin" element={<AdminDashboard />}>
            <Route path="createAccounts" element={<CreateAccounts />} />
            <Route path="viewAccounts" element={<ViewAccounts />} />
          </Route>

        </>
      )}
    </Routes>
  );
}

export default App;











// import React from 'react';
// import { Routes, Route, useLocation } from 'react-router-dom';
// import Home from './pages/Auth/Home/Home';
// import Login from './pages/Auth/login';
// import SignUp from './pages/Auth/signup';
// import Dashboard from './pages/dashboard/Dashboard';
// import CreateAccounts from './pages/dashboard/accounts';
// import ViewAccounts from './pages/dashboard/viewAccounts';
// import ViewTransactions from './pages/dashboard/viewTransactions';
// import LandingPage from './component/landingpage/LandingPage';
// import AdminDashboard from './component/admin/AdminDashboard';
// import Create from './component/admin/Create';
// import Read from './component/admin/Read';
// import Update from './component/admin/Update';
// import Delete from './component/admin/Delete';

// function App() {
//   const location = useLocation();

//   return (
//     <Routes>
//       {location.pathname === '/' ? (
//         <Route path="/" element={<LandingPage />} />
//       ) : (
//         <>
//           <Route path="/home" element={<Home />} />
//           <Route path="/login" element={<Login />} />
//           <Route path="/signup" element={<SignUp />} />
//           <Route path="/dashboard" element={<Dashboard />}>
//             <Route path="createAccounts" element={<CreateAccounts />} />
//             <Route path="viewAccounts" element={<ViewAccounts />} />
//             <Route path="viewTransactions" element={<ViewTransactions />} />
//           </Route>
//           <Route path="/admin" element={<AdminDashboard />}>
//             <Route path="create" element={<Create />} />
//             <Route path="read" element={<Read />} />
//             <Route path="update" element={<Update />} />
//             <Route path="delete" element={<Delete />} />
//           </Route>
//         </>
//       )}
//     </Routes>
//   );
// }

// export default App;
