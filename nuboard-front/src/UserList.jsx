import { useState } from "react";
import { useEffect } from "react";
import axios from 'axios';


function UserList(){
    const [users, setUsers] = useState([]);

    function fetchUsers() {
        axios.get('http://localhost:8080/api/users')
            .then((response) => {
                if (response.data.code !== 0){
                    setUsers(response.data.data);
                }
                else {
                    console.log(response.data.message);
                }
            })
            .catch((error) => {
                if (error.response) {
                    console.error(error.response.status);
                    console.error(error.response.data);
                } else if (error.request) {
                // No response after request
                console.error('No response', error.request);
                } else {
                // Failed to request
                console.error('Failed to request', error.message);
                }
            });
    }

    useEffect(() => {
        fetchUsers();
    }, []);

    return (
        <div>
            <h1>All Users</h1>
            <ul>
                {users.map(user =>
                    <li key={user.id}>
                        <span>{user.id}</span> | <span>{user.name}</span>
                    </li>)}
            </ul>
        </div>
    );
}

export default UserList;