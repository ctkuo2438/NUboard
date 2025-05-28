import { useState } from "react";
import { useEffect } from "react";
import axios from 'axios';

function CollegeList(){
    const [colleges, setColleges] = useState([]);

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

    useEffect(() => {
        fetchColleges();
    }, []);

    return (
        <div>
            <h1>All colleges</h1>
            <ul>
                {colleges.map(college =>
                    <li key={college.id}>
                        <span>{college.id}</span> | <span>{college.name}</span>
                    </li>)}
            </ul>
        </div>
    );
}

export default CollegeList;