import React, { useState, useEffect } from 'react';
import { useDispatch } from 'react-redux';
import { addNew } from '../admin_page/studentsSlice';

const AddStud = () => {
    const dispatch = useDispatch();

    const [formData, setFormData] = useState({
        name: '',
        age: '',
    });

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData((prevData) => ({ ...prevData, [name]: value }));
    };

    useEffect(() => {
        dispatch(addNew(formData));
    }, [dispatch, formData]);

    return (
        <form>
            <div className="form-group">
                <label htmlFor="name">Student's name: </label>
                <input
                    type="text"
                    className="form-control"
                    name="name"
                    value={formData.name}
                    onChange={handleInputChange}
                />
            </div>
            <div className="form-group">
                <label htmlFor="age">Age: </label>
                <input
                    type="text"
                    className="form-control"
                    name="age"
                    value={formData.age}
                    onChange={handleInputChange}
                />
            </div>
            <div style={{ textAlign: 'left', marginTop: '10px' }}>
                <button type="submit" className="btn btn-primary">
                    Submit
                </button>
            </div>
        </form>
    );
};

export default AddStud;
