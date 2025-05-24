import React, {useId, useRef, useState, useEffect } from 'react'
import axios from 'axios';

function NUboard(){
    const [events, setEvents] = useState([]);
    const [selectedEvent, setSelectedEvent] = useState(null);
    const [registrationRecords, setRegistrationRecords] = useState([]);
    const [registeringUser, setRegisteringUser] = useState("");
    const [newEvent, setNewEvent] = useState({
        title: "",
        description: "",
        startTime: "",
        endTime: "",
        location: "",
        address: "",
        creatorId:"",
        organizerType:"SCHOOL",
    });
    const creationRef = useRef();
    const registrationRef = useRef();
    const id = useId();

  async function saveEvent() {
    try {
        await axios.post('http://localhost:8080/api/events', {
            title: newEvent.title,
            description: newEvent.description,
            startTime: newEvent.startTime,
            endTime: newEvent.endTime,
            location: newEvent.location,
            address: newEvent.address,
            creatorId: newEvent.creatorId,
            organizerType: newEvent.organizerType,
        });
  
        // Only saving after successful submission
        setNewEvent({
          title: "",
          description: "",
          startTime: "",
          endTime: "",
          location: "",
          address: "",
          creatorId:"",
          organizerType:"SCHOOL",
        });
  
        creationRef.current.close();

        fetchEvents();
    }
    catch (error) {
        console.error("Failed to save event:", error);
        alert("Fail to save, please try again");
    }
  }

  function inputNewEvent(e, field){
    setNewEvent(prev => ({
      ...prev,
      [field]:e.target.value,
    }))
  }

  function fetchEvents() {
    axios.get('http://localhost:8080/api/events')
      .then((response) => setEvents(response.data))
      .catch((error) => console.error("Failed to load events", error));
  }

  function deleteEvents(id){
    axios.delete(`http://localhost:8080/api/events/${id}`)
        .then(() => {
          console.log("delete successfully");
          fetchEvents();
        })
        .catch((err) => {
          console.error("Fail to delete, please try again", err);
        });
  }

  function openRegistrationModal(event) {
    setSelectedEvent(event);
    setRegistrationRecords([]);
    registrationRef.current.showModal();
  
    axios.get(`http://localhost:8080/api/registrations/${event.id}`)
      .then((response) => {
        setRegistrationRecords(response.data);
      })
      .catch((error) => console.error("Failed to load registration records", error));
  }

  function register(userId, eventId){
    axios.post('http://localhost:8080/api/registrations', null, {
      params: {
        userId: userId,
        eventId: eventId
      }
    })
    .then(response => {
      alert(response.data);
      openRegistrationModal(selectedEvent);
      setRegisteringUser("");
    })
    .catch(error => {
      console.error('Error registering:', error);
    });
  }
  

  useEffect(() => {
    fetchEvents();
  }, [])

    return (
    <>
      <div className='form'>
        <button type="button" onClick={() => creationRef.current.showModal()} className='create-button'>Create Event</button>
        <dialog ref={creationRef}>
          <div>
            <label htmlFor={`${id}-title`}>Title: </label>
            <input type="text" id={`${id}-title`} onChange={(e) => inputNewEvent(e, "title")} />
          </div>

          <div>
            <label htmlFor={`${id}-description`}>Description: </label>
            <input type="text" id={`${id}-description`} onChange={(e) => inputNewEvent(e, "description")}/>
          </div>

          <div>
            <label htmlFor={`${id}-location`}>Location: </label>
            <input type="text" id={`${id}-location`} onChange={(e) => inputNewEvent(e, "location")}/>
          </div>

          <div>
            <label htmlFor={`${id}-location`}>Address: </label>
            <input type="text" id={`${id}-location`} onChange={(e) => inputNewEvent(e, "address")}/>
          </div>

          <div>
            <label htmlFor={`${id}-creator-id`}>Creator ID: </label>
            <input type="text" id={`${id}-creator-id`} onChange={(e) => inputNewEvent(e, "creatorId")}/>
          </div>

          <div>
            <label htmlFor={`${id}-start-time`}>StartTime: </label>
            <input 
              type="text" 
              id={`${id}-start-time`}
              placeholder='Example: 2025-05-10T19:00:00'
              onChange={(e) => inputNewEvent(e, "startTime")}
            />
          </div>

          <div>
            <label htmlFor={`${id}-end-time`}>EndTime: </label>
            <input 
              type="text" 
              id={`${id}-end-time`}
              placeholder='Example: 2025-05-10T19:00:00'
              onChange={(e) => inputNewEvent(e, "endTime")}
            />
          </div>

          <button type="button" onClick={saveEvent}>
            Save
          </button>

          <button type="button" onClick={() => creationRef.current.close()}>
            Close
          </button>
        </dialog>

        <p>title</p>
        <p>description</p>
        <p>start time</p>
        <p>end time</p>
        <p>Location</p>
        <p>address</p>
        <p>creatorid</p>
        <p>organizerType</p>
        <p></p>

        {events.map((event) => 
          <React.Fragment key={event.id}>
            <p>{event.title}</p>
            <p>{event.description}</p>
            <p>{event.startTime}</p>
            <p>{event.endTime}</p>
            <p>{event.location}</p>
            <p>{event.address}</p>
            <p>{event.creatorId}</p>
            <p>{event.organizerType}</p>

            <div>
              <button type="button" onClick={() => openRegistrationModal(event)}>Register</button>
              <button type="button" onClick={() => deleteEvents(event.id)}>Delete</button>
            </div>

          </React.Fragment>
        )}

        <dialog ref={registrationRef}>
          <h2>Registered Users</h2>
          {registrationRecords.length === 0 ? (
            <p>No Records For Now</p>
            ) : (
            registrationRecords.map((record) => (
              <p>{record.user.id}</p>
            ))
          )}

          <label htmlFor={`${id}-registering-user`}>Who wants to register?</label>
          <input
            type="text"
            id={`${id}-registering-user`} 
            onChange={(e) => setRegisteringUser(e.target.value)} 
            value={registeringUser}
            />
          <button type="button" onClick={(e) => register(registeringUser, selectedEvent.id)}>
            Register
          </button>

          <button type="button" onClick={() => registrationRef.current.close()}>
            Close
          </button>
        </dialog>

      </div>
    </>
  );
}

export default NUboard;

