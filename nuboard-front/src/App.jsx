import { useState } from 'react';

import NUboard from './NUboard';

import CreateUser from './CreateUser.jsx';
import SearchUser from './SearchUser.jsx';
import UserList from './UserList.jsx';
import CollegeList from './CollegeList.jsx';
import LocationList from './LocationList.jsx';
import UpdateUser from './UpdateUser.jsx';
import DeleteUser from './DeleteUser.jsx';


import './App.css'

function App() {
  const [page, setPage] = useState('/');
  return (
    <div>
      <button onClick={() => setPage('/')}>NUboard</button>
      <button onClick={() => setPage('/create-user')}>Create User</button>
      <button onClick={() => setPage('/update-user')}>Update User</button>
      <button onClick={() => setPage('/search-user')}>Search User</button>
      <button onClick={() => setPage('/delete-user')}>Delete User</button>
      <button onClick={() => setPage('/user-list')}>User List</button>
      <button onClick={() => setPage('/college-list')}>College List</button>
      <button onClick={() => setPage('/location-list')}>Location List</button>

      {page == '/' && <NUboard />}
      {page == '/create-user' && <CreateUser />}
      {page == '/update-user' && <UpdateUser />}
      {page == '/search-user' && <SearchUser />}
      {page == '/delete-user' && <DeleteUser />}
      {page == '/user-list' && <UserList />}
      {page == '/college-list' && <CollegeList />}
      {page == '/location-list' && <LocationList />}

    </div>

  )
}

export default App