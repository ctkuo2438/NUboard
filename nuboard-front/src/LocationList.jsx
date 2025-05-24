import { useState } from "react";
import { useEffect } from "react";
import axios from 'axios';

function LocationList(){
    const [locations, setLocations] = useState([]);

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

    useEffect(() => {
        fetchLocations();
    }, []);

    return (
        <div>
            <h1>All locations</h1>
            <ul>
                {locations.map(location =>
                    <li key={location.id}>
                        <span>{location.id}</span> | <span>{location.name}</span>
                    </li>)}
            </ul>
        </div>
    );
}

export default LocationList;
