import { useState } from 'react';

import NUboard from './NUboard';
import EventManagement from './EventManagement';
import UserManagement from './UserManagement';

import './App.css'

function App() {
  const [page, setPage] = useState('/');
  return (
    <div>
      <button onClick={() => setPage('/')}>NUboard</button>
      <button onClick={() => setPage('/event')}>Event Management</button>
      <button onClick={() => setPage('/user')}>User Management</button>
      {page == '/' && <NUboard />}
      {page == '/event' && <EventManagement />}
      {page == '/user' && <UserManagement />}
    </div>

  )
}

export default App