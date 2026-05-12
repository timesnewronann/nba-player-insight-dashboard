import { useState } from "react";
import type { Player } from "../types/Player";
import { useParams } from "react-router-dom";

export default function PlayerPage() {
    const params = useParams();

    return (
        <div>
            <h1>Player Page</h1>
            <h2>Player Id: {params.id}</h2>
        </div>
    )

}

