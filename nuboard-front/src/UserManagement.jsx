import { useState } from "react";

function UserManagement(){
    const[newUser, setNewUser] = useState({
        username: "",
        email: "",
        program: "",
        locationId: "",
        collegeId: "",
    });

    function inputNewUser(e, field){
        setNewUser(prev => ({
        ...prev,
        [field]:e.target.value,
        }))
    }

    const [users, setUsers] = useState([]);
    const [locations, setLocations] = useState([]);
    const [colleges, setColleges] = useState([]);
    
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

    function fetchColleges() {
        axios.get('http://localhost:8080/api/colleges')
            .then((response) => {
                if (response.data.code !== 0){
                    setColleges(response.data.data);
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

    function fetchLocations() {
    axios.get('http://localhost:8080/api/locations')
        .then((response) => {
            if (response.data.code !== 0){
                setLocations(response.data.data);
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

    async function createUser(){
        try {
            await axios.post('http://localhost:8080/api/users', {
                username: newUser.username,
                email: newUser.email,
                program: newUser.program,
                locationId: Number(newUser.locationId),
                collegeId: Number(newUser.collegeId),
            });
    
            // Only saving after successful submission
            setNewUser({
                username: "",
                email: "",
                program: "",
                locationId: "",
                collegeId: "",
            });

            fetchUsers();
        }
        catch (error) {
            console.error("Failed to save:", error);
            alert("Fail to save, please try again");
        }
    }


    useEffect(() => {
        fetchColleges();
        fetchUsers();
        fetchLocations();
    }, []);




    return (
        <div>
            <div>
                <h1>All locations</h1>
                <ul>
                    {locations.map(location =>
                        <li key={location.id}>
                            <span>{location.id}</span> | <span>{location.name}</span>
                        </li>)}
                </ul>
            </div>

            <div>
                <h1>All colleges</h1>
                <ul>
                    {colleges.map(college =>
                        <li key={college.id}>
                            <span>{college.id}</span> | <span>{college.name}</span>
                        </li>)}
                </ul>
            </div>

            <div>
                <div>
                    <input 
                        type="text"
                        placeholder='username'
                        onChange={(e) => inputNewEvent(e, "username")} 
                    />

                    <input 
                        type="text"
                        placeholder='email'
                        onChange={(e) => inputNewEvent(e, "email")} 
                    />

                    <input 
                        type="text"
                        placeholder='program'
                        onChange={(e) => inputNewEvent(e, "email")} 
                    />

                    <input 
                        type="text"
                        placeholder='locationId'
                        onChange={(e) => inputNewEvent(e, "locationId")} 
                    />

                    <input 
                        type="text"
                        placeholder='collegeId'
                        onChange={(e) => inputNewEvent(e, "collegeId")} 
                    />
                </div>
                <button type="button" onClick={createUser}>Create User</button>
            </div>

            <div>
                <h1>All Users</h1>
                <ul>
                    {users.map(user =>
                        <li key={user.id}>
                            <span>{user.id}</span> | <span>{user.name}</span>
                        </li>)}
                </ul>
            </div>

            <ul>
                {users.map(user => <li key={user.id}>{user.name}</li>)}
            </ul>
        </div>
    );
}

export default UserManagement;
