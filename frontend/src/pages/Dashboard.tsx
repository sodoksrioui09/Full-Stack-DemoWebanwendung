import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../services/api"; // your Axios instance with JWT interceptor

interface Task {
    id: string;
    title: string;
    description: string;
    status: string;
}

const Dashboard: React.FC = () => {
    const navigate = useNavigate();
    const [tasks, setTasks] = useState<Task[]>([]);

    // Create task form state
    const [title, setTitle] = useState("");
    const [description, setDescription] = useState("");

    // Edit task state
    const [editingId, setEditingId] = useState<string | null>(null);
    const [editingTitle, setEditingTitle] = useState("");
    const [editingDescription, setEditingDescription] = useState("");

    // Logout
    const handleLogout = () => {
        localStorage.removeItem("token");
        navigate("/");
    };

    // Fetch tasks from backend
    const fetchTasks = () => {
        api.get("/tasks")
            .then((res) => setTasks(res.data))
            .catch((err) => console.error(err));
    };

    useEffect(() => {
        fetchTasks();
    }, []);

    // Create a new task
    const handleCreateTask = (e: React.FormEvent) => {
        e.preventDefault();
        api.post("/tasks", {
            title,
            description,
            status: "OPEN",
            createdAt: new Date().toISOString(),
            updatedAt: new Date().toISOString(),
        })
            .then(() => {
                setTitle("");
                setDescription("");
                fetchTasks();
            })
            .catch((err) => console.error(err));
    };

    // Start editing a task
    const handleEditTask = (task: Task) => {
        setEditingId(task.id);
        setEditingTitle(task.title);
        setEditingDescription(task.description);
    };

    // Update a task
    const handleUpdateTask = () => {
        if (!editingId) return;
        api.put(`/tasks/${editingId}`, {
            title: editingTitle,
            description: editingDescription,
            status: "OPEN",
            createdAt: new Date().toISOString(), // important
            updatedAt: new Date().toISOString(),
        })
            .then(() => {
                setEditingId(null);
                fetchTasks();
            })
            .catch((err) => console.error(err));
    };

    // Delete a task
    const handleDeleteTask = (id: string) => {
        api.delete(`/tasks/${id}`)
            .then(() => fetchTasks())
            .catch((err) => console.error(err));
    };

    return (
        <div className="flex flex-col items-center justify-start min-h-screen bg-gray-50 p-4">
            <h1 className="text-3xl font-bold mb-4">Dashboard 🎉</h1>

            <button
                onClick={handleLogout}
                className="bg-red-500 text-white px-4 py-2 rounded-lg hover:bg-red-600 mb-6"
            >
                Logout
            </button>

            {/* Create Task Form */}
            <form
                onSubmit={handleCreateTask}
                className="mb-6 w-full max-w-md bg-white p-4 rounded-lg shadow"
            >
                <h2 className="text-xl font-semibold mb-2">Create New Task</h2>
                <input
                    type="text"
                    placeholder="Title"
                    value={title}
                    onChange={(e) => setTitle(e.target.value)}
                    className="border p-2 mb-2 w-full rounded"
                    required
                />
                <textarea
                    placeholder="Description"
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                    className="border p-2 mb-2 w-full rounded"
                    required
                />
                <button
                    type="submit"
                    className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
                >
                    Add Task
                </button>
            </form>

            {/* Tasks List */}
            <h2 className="text-2xl font-semibold mb-2">Tasks</h2>
            <ul className="space-y-2 w-full max-w-md">
                {tasks.map((task) => (
                    <li
                        key={task.id}
                        className="border p-2 rounded-lg bg-white flex justify-between items-center"
                    >
                        <div>
                            {editingId === task.id ? (
                                <>
                                    <input
                                        value={editingTitle}
                                        onChange={(e) => setEditingTitle(e.target.value)}
                                        className="border p-1 rounded mr-2"
                                    />
                                    <input
                                        value={editingDescription}
                                        onChange={(e) => setEditingDescription(e.target.value)}
                                        className="border p-1 rounded"
                                    />
                                </>
                            ) : (
                                <>
                                    <strong>{task.title}</strong> - {task.description} (
                                    {task.status})
                                </>
                            )}
                        </div>
                        <div className="space-x-2">
                            {editingId === task.id ? (
                                <button
                                    className="bg-green-500 text-white px-2 py-1 rounded hover:bg-green-600"
                                    onClick={handleUpdateTask}
                                >
                                    Save
                                </button>
                            ) : (
                                <button
                                    className="bg-yellow-500 text-white px-2 py-1 rounded hover:bg-yellow-600"
                                    onClick={() => handleEditTask(task)}
                                >
                                    Edit
                                </button>
                            )}
                            <button
                                className="bg-red-500 text-white px-2 py-1 rounded hover:bg-red-600"
                                onClick={() => handleDeleteTask(task.id)}
                            >
                                Delete
                            </button>
                        </div>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default Dashboard;
