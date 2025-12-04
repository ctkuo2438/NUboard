import React, { useId, useRef, useState, useEffect } from 'react';
import axios from 'axios';

function NUboard() {
    const [events, setEvents] = useState([]);
    const [selectedEvent, setSelectedEvent] = useState(null);
    const [registrationRecords, setRegistrationRecords] = useState([]);
    const [registeringUser, setRegisteringUser] = useState("");
    const [isUpdating, setIsUpdating] = useState(false);
    const [loading, setLoading] = useState(true);
    const [newEvent, setNewEvent] = useState({
        title: "",
        description: "",
        startTime: "",
        endTime: "",
        locationId: "",
        address: "",
        creatorId: "",
        organizerType: "SCHOOL",
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
                locationId: newEvent.locationId,
                address: newEvent.address,
                creatorId: newEvent.creatorId,
                organizerType: newEvent.organizerType,
            });

            setNewEvent({
                title: "",
                description: "",
                startTime: "",
                endTime: "",
                locationId: "",
                address: "",
                creatorId: "",
                organizerType: "SCHOOL",
            });

            creationRef.current.close();
            fetchEvents();
        } catch (error) {
            console.error("Failed to save event:", error);
            alert("Failed to save event. Please try again.");
        }
    }

    async function updateEvent() {
        try {
            const eventData = {
                title: document.getElementById(`${id}-title`).value,
                description: document.getElementById(`${id}-description`).value,
                startTime: document.getElementById(`${id}-start-time`).value,
                endTime: document.getElementById(`${id}-end-time`).value,
                locationId: document.getElementById(`${id}-location-id`).value,
                address: document.getElementById(`${id}-address`).value,
                creatorId: document.getElementById(`${id}-creator-id`).value,
                organizerType: "SCHOOL"
            };

            await axios.put(`http://localhost:8080/api/events/${selectedEvent}`, eventData);
            await fetchEvents();
            creationRef.current.close();
            setIsUpdating(false);
            setSelectedEvent(null);
        } catch (error) {
            console.error("Failed to update event:", error);
            alert("Failed to update event. Please try again.");
        }
    }

    function openUpdateModal(event) {
        setIsUpdating(true);
        setSelectedEvent(event.id);
        creationRef.current.showModal();
        setTimeout(() => {
            document.getElementById(`${id}-title`).value = event.title;
            document.getElementById(`${id}-description`).value = event.description;
            document.getElementById(`${id}-location-id`).value = event.locationId;
            document.getElementById(`${id}-address`).value = event.address;
            document.getElementById(`${id}-creator-id`).value = event.creatorId;
            document.getElementById(`${id}-start-time`).value = event.startTime;
            document.getElementById(`${id}-end-time`).value = event.endTime;
        }, 0);
    }

    function inputNewEvent(e, field) {
        setNewEvent(prev => ({
            ...prev,
            [field]: e.target.value,
        }));
    }

    async function fetchEvents() {
        setLoading(true);
        try {
            const response = await axios.get('http://localhost:8080/api/events');
            setEvents(response.data.data || []);
        } catch (error) {
            console.error("Failed to load events", error);
        } finally {
            setLoading(false);
        }
    }

    function deleteEvents(eventId) {
        if (window.confirm('Are you sure you want to delete this event?')) {
            axios.delete(`http://localhost:8080/api/events/${eventId}`)
                .then(() => {
                    fetchEvents();
                })
                .catch((err) => {
                    console.error("Failed to delete event", err);
                    alert("Failed to delete event. Please try again.");
                });
        }
    }

    function openRegistrationModal(event) {
        setSelectedEvent(event);
        setRegistrationRecords([]);
        registrationRef.current.showModal();

        axios.get(`http://localhost:8080/api/registrations/event/${event.id}`)
            .then((response) => {
                setRegistrationRecords(response.data.data || []);
            })
            .catch((error) => console.error("Failed to load registration records", error));
    }

    function register(userId, eventId) {
        axios.post('http://localhost:8080/api/registrations/register', null, {
            params: { userId, eventId }
        })
            .then(response => {
                alert(response.data.data);
                openRegistrationModal(selectedEvent);
                setRegisteringUser("");
            })
            .catch(error => {
                console.error('Error registering:', error);
                alert("Failed to register. Please try again.");
            });
    }

    useEffect(() => {
        fetchEvents();
    }, []);

    async function handleSave() {
        if (isUpdating) {
            await updateEvent();
        } else {
            await saveEvent();
        }
    }

    function clearForm() {
        const fields = ['title', 'description', 'location-id', 'address', 'creator-id', 'start-time', 'end-time'];
        fields.forEach(field => {
            const element = document.getElementById(`${id}-${field}`);
            if (element) element.value = "";
        });
    }

    function formatDateTime(dateTimeStr) {
        if (!dateTimeStr) return 'N/A';
        try {
            const date = new Date(dateTimeStr);
            return date.toLocaleString('en-US', {
                weekday: 'short',
                month: 'short',
                day: 'numeric',
                hour: 'numeric',
                minute: '2-digit',
                hour12: true
            });
        } catch {
            return dateTimeStr;
        }
    }

    return (
        <div className="nuboard-container">
            {/* Page Header */}
            <div className="page-header">
                <div>
                    <h1 className="page-title">Events</h1>
                    <p className="page-subtitle">Discover and manage campus events</p>
                </div>
                <button
                    type="button"
                    onClick={() => {
                        setIsUpdating(false);
                        setSelectedEvent(null);
                        clearForm();
                        creationRef.current.showModal();
                    }}
                    className="create-button"
                >
                    Create Event
                </button>
            </div>

            {/* Stats Cards */}
            <div className="stats-grid">
                <div className="stat-card">
                    <div className="stat-icon primary">📅</div>
                    <div className="stat-content">
                        <div className="stat-value">{events.length}</div>
                        <div className="stat-label">Total Events</div>
                    </div>
                </div>
                <div className="stat-card">
                    <div className="stat-icon success">✅</div>
                    <div className="stat-content">
                        <div className="stat-value">{events.filter(e => new Date(e.endTime) > new Date()).length}</div>
                        <div className="stat-label">Upcoming</div>
                    </div>
                </div>
                <div className="stat-card">
                    <div className="stat-icon info">🏫</div>
                    <div className="stat-content">
                        <div className="stat-value">{events.filter(e => e.organizerType === 'SCHOOL').length}</div>
                        <div className="stat-label">School Events</div>
                    </div>
                </div>
            </div>

            {/* Events Grid */}
            {loading ? (
                <div className="loading-overlay" style={{ position: 'relative', minHeight: '200px' }}>
                    <div className="spinner"></div>
                </div>
            ) : events.length === 0 ? (
                <div className="empty-state">
                    <div className="empty-state-icon">📅</div>
                    <h3 className="empty-state-title">No Events Yet</h3>
                    <p className="empty-state-description">Create your first event to get started!</p>
                    <button
                        className="btn btn-primary"
                        onClick={() => {
                            setIsUpdating(false);
                            clearForm();
                            creationRef.current.showModal();
                        }}
                    >
                        Create Event
                    </button>
                </div>
            ) : (
                <div className="events-container">
                    {events.map((event) => (
                        <div key={event.id} className="event-card">
                            <div className="event-card-header">
                                <h3 className="event-card-title">{event.title}</h3>
                                <div className="event-card-organizer">
                                    <span className="badge badge-primary">{event.organizerType}</span>
                                </div>
                            </div>
                            <div className="event-card-body">
                                <p className="event-card-description">
                                    {event.description || 'No description provided'}
                                </p>
                                <div className="event-card-details">
                                    <div className="event-detail-item">
                                        <span className="event-detail-icon">🕐</span>
                                        <div>
                                            <strong>Start:</strong> {formatDateTime(event.startTime)}
                                        </div>
                                    </div>
                                    <div className="event-detail-item">
                                        <span className="event-detail-icon">🕑</span>
                                        <div>
                                            <strong>End:</strong> {formatDateTime(event.endTime)}
                                        </div>
                                    </div>
                                    <div className="event-detail-item">
                                        <span className="event-detail-icon">📍</span>
                                        <div>{event.address || 'Location TBD'}</div>
                                    </div>
                                </div>
                            </div>
                            <div className="event-card-footer">
                                <button
                                    className="btn btn-primary btn-sm"
                                    onClick={() => openRegistrationModal(event)}
                                >
                                    Register
                                </button>
                                <button
                                    className="btn btn-secondary btn-sm"
                                    onClick={() => openUpdateModal(event)}
                                >
                                    Edit
                                </button>
                                <button
                                    className="btn btn-danger btn-sm"
                                    onClick={() => deleteEvents(event.id)}
                                >
                                    Delete
                                </button>
                            </div>
                        </div>
                    ))}
                </div>
            )}

            {/* Create/Update Event Modal */}
            <dialog ref={creationRef} className="modal">
                <div className="modal-content">
                    <div className="modal-header">
                        <h2 className="modal-title">
                            {isUpdating ? '✏️ Update Event' : '➕ Create New Event'}
                        </h2>
                        <button
                            className="modal-close"
                            onClick={() => {
                                setIsUpdating(false);
                                creationRef.current.close();
                            }}
                        >
                            ×
                        </button>
                    </div>

                    <div className="modal-body">
                        <div className="form-group">
                            <label className="form-label" htmlFor={`${id}-title`}>Event Title</label>
                            <input
                                type="text"
                                id={`${id}-title`}
                                className="form-input"
                                placeholder="Enter event title"
                                onChange={(e) => inputNewEvent(e, "title")}
                            />
                        </div>

                        <div className="form-group">
                            <label className="form-label" htmlFor={`${id}-description`}>Description</label>
                            <textarea
                                id={`${id}-description`}
                                className="form-input form-textarea"
                                placeholder="Describe your event"
                                onChange={(e) => inputNewEvent(e, "description")}
                            />
                        </div>

                        <div className="form-row">
                            <div className="form-group">
                                <label className="form-label" htmlFor={`${id}-location-id`}>Location ID</label>
                                <input
                                    type="text"
                                    id={`${id}-location-id`}
                                    className="form-input"
                                    placeholder="Location ID"
                                    onChange={(e) => inputNewEvent(e, "locationId")}
                                />
                            </div>

                            <div className="form-group">
                                <label className="form-label" htmlFor={`${id}-address`}>Address</label>
                                <input
                                    type="text"
                                    id={`${id}-address`}
                                    className="form-input"
                                    placeholder="Event address"
                                    onChange={(e) => inputNewEvent(e, "address")}
                                />
                            </div>
                        </div>

                        <div className="form-group">
                            <label className="form-label" htmlFor={`${id}-creator-id`}>Creator ID</label>
                            <input
                                type="text"
                                id={`${id}-creator-id`}
                                className="form-input"
                                placeholder="Your user ID"
                                onChange={(e) => inputNewEvent(e, "creatorId")}
                            />
                        </div>

                        <div className="form-row">
                            <div className="form-group">
                                <label className="form-label" htmlFor={`${id}-start-time`}>Start Time</label>
                                <input
                                    type="datetime-local"
                                    id={`${id}-start-time`}
                                    className="form-input"
                                    onChange={(e) => inputNewEvent(e, "startTime")}
                                />
                            </div>

                            <div className="form-group">
                                <label className="form-label" htmlFor={`${id}-end-time`}>End Time</label>
                                <input
                                    type="datetime-local"
                                    id={`${id}-end-time`}
                                    className="form-input"
                                    onChange={(e) => inputNewEvent(e, "endTime")}
                                />
                            </div>
                        </div>
                    </div>

                    <div className="modal-footer">
                        <button
                            className="btn btn-secondary"
                            onClick={() => {
                                setIsUpdating(false);
                                creationRef.current.close();
                            }}
                        >
                            Cancel
                        </button>
                        <button className="btn btn-primary" onClick={handleSave}>
                            {isUpdating ? 'Update Event' : 'Create Event'}
                        </button>
                    </div>
                </div>
            </dialog>

            {/* Registration Modal */}
            <dialog ref={registrationRef} className="modal">
                <div className="modal-content">
                    <div className="modal-header">
                        <h2 className="modal-title">📋 Event Registration</h2>
                        <button
                            className="modal-close"
                            onClick={() => registrationRef.current.close()}
                        >
                            ×
                        </button>
                    </div>

                    <div className="modal-body">
                        <h4 style={{ marginBottom: 'var(--spacing-md)' }}>
                            {selectedEvent?.title || 'Event'}
                        </h4>

                        <div className="registration-list">
                            <h5 style={{ color: 'var(--gray-600)', marginBottom: 'var(--spacing-md)' }}>
                                Registered Users ({registrationRecords.length})
                            </h5>
                            {registrationRecords.length === 0 ? (
                                <p className="text-muted" style={{ textAlign: 'center', padding: 'var(--spacing-lg)' }}>
                                    No registrations yet
                                </p>
                            ) : (
                                registrationRecords.map((record, index) => (
                                    <div key={index} className="registration-item">
                                        <div className="registration-user">
                                            <div className="registration-avatar">
                                                {String(record.userId).slice(0, 2)}
                                            </div>
                                            <div className="registration-info">
                                                <span className="registration-name">User #{record.userId}</span>
                                            </div>
                                        </div>
                                    </div>
                                ))
                            )}
                        </div>

                        <div className="form-group" style={{ marginTop: 'var(--spacing-lg)' }}>
                            <label className="form-label" htmlFor={`${id}-registering-user`}>
                                Register New User
                            </label>
                            <div style={{ display: 'flex', gap: 'var(--spacing-md)' }}>
                                <input
                                    type="text"
                                    id={`${id}-registering-user`}
                                    className="form-input"
                                    placeholder="Enter User ID"
                                    onChange={(e) => setRegisteringUser(e.target.value)}
                                    value={registeringUser}
                                />
                                <button
                                    className="btn btn-primary"
                                    onClick={() => register(registeringUser, selectedEvent?.id)}
                                    disabled={!registeringUser}
                                >
                                    Register
                                </button>
                            </div>
                        </div>
                    </div>

                    <div className="modal-footer">
                        <button
                            className="btn btn-secondary"
                            onClick={() => registrationRef.current.close()}
                        >
                            Close
                        </button>
                    </div>
                </div>
            </dialog>
        </div>
    );
}

export default NUboard;
