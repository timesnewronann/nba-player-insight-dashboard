import {useRef, useEffect} from "react"
import * as d3 from "d3"
import type { ShotChart } from "../types/ShotChart"

interface Props {
    shots: ShotChart[]
}

export default function ShotChartComponent({shots}: Props) {
    const svgRef = useRef<SVGSVGElement>(null)

    useEffect(() => {
        if (!svgRef.current || shots.length === 0) return

        const width = 500
        const height = 470

        // Select the SVG and clear previous draws
        const svg = d3.select(svgRef.current)
        svg.selectAll("*").remove()

        // Set background color
        svg.append("rect")
            .attr("width", width)
            .attr("height", height)
            .attr("fill", "#1A1410")

        // Scale functions
        const xScale = d3.scaleLinear().domain([-250, 250]).range([0, width])
        const yScale = d3.scaleLinear().domain([-50, 420]).range([0, height])

        // Draw court outline 
        svg.append("rect")
            .attr("x", xScale(-250)).attr("y", yScale(-50))
            .attr("width", width).attr("height", height)
            .attr("fill", "none")
            .attr("stroke", "#C8A96E").attr("stroke-width", 1).attr("opacity", 0.4)
        
        // Draw the basket
    }, [shots])

    return (
        <svg ref={svgRef} width={500} height={470} />
    )
}