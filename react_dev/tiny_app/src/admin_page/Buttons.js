import React, { useState } from 'react';
import Button from 'react-bootstrap/Button';
import { useSelector, useDispatch } from 'react-redux';
import { showStudents, addNew, buttonState } from './buttonSlice';

export function Buttons() {
    const dispatch = useDispatch();
    const count = useSelector(buttonState);
    return (
        <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
            <Button as="a" variant="primary" style={{ marginBottom: '10px' }}
            onClick={() => dispatch(showStudents())}>
                Show students
            </Button>
            
            <Button as="a" variant="success" style={{ marginBottom: '10px' }}
            onClick={() => dispatch(addNew())}>
                Add a new student
            </Button>
            <span>{count}</span>
        </div>
    );
}   