import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import '../App.css';
import { useSelector, useDispatch } from 'react-redux';
import { useEffect } from 'react';
import { addAll } from '../admin_page/studentsSlice';

const StudentsTable = () => {
    const students = useSelector((state) => state.students.value)
    const dispatch = useDispatch()

    useEffect(() => {
        const fetchDataFromURL = async () => {
            try {
                const response = await fetch('http://localhost:8088/api/v1/student/all');
                const data = await response.json();
                dispatch(addAll(data));
            } catch (error) {
                console.error('Error fetching data:', error);
            }
        };
        fetchDataFromURL();
    }, []);

    return (
        <div>
            <div className="my-4"></div>
            <h1>Student's Table</h1>
            <table className="table">
                <thead>
                    <tr>
                        <th scope="col">#</th>
                        <th scope="col">Name</th>
                        <th scope="col">Age</th>
                        <th scope="col">Id</th>
                    </tr>
                </thead>
                <tbody>
                    {students.map((entry, index) => (
                        <tr key={index}>
                            <th scope="row">{index + 1}</th>
                            <td>{entry.name}</td>
                            <td>{entry.age}</td>
                            <td>@{entry.name.substring(0, 3)}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
};


export default StudentsTable;
