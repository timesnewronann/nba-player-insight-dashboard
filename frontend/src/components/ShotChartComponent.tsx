import {useRef, useEffect} from "react"
import * as d3 from "d3"
import type { ShotChart } from "../types/ShotChart"
import { hexbin } from "d3-hexbin"

interface Props {
    shots: ShotChart[]
    mode: "dots" | "hexbin"
}

export default function ShotChartComponent({shots, mode}: Props) {
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
        const yScale = d3.scaleLinear().domain([420, -50]).range([0, height])

        // Draw court outline 
        svg.append("rect")
            .attr("x", xScale(-250)).attr("y", yScale(-50))
            .attr("width", width).attr("height", height)
            .attr("fill", "none")
            .attr("stroke", "#C8A96E").attr("stroke-width", 1).attr("opacity", 0.4)
        
        // Draw the basket
        svg.append("circle")
            .attr("cx", xScale(0)).attr("cy", yScale(0))
            .attr("r", 7.5)
            .attr("fill", "none")
            .attr("stroke", "#C8A96E").attr("stroke-width", 1.5).attr("opacity", 0.6)

        // Draw the paint (key)
        svg.append("rect")
            .attr("x", xScale(-80))
            .attr("y", Math.min(yScale(-50), yScale(140)))
            .attr("width", xScale(80) - xScale(-80))
            .attr("height", Math.abs(yScale(140) - yScale(-50)))
            .attr("fill", "none")
            .attr("stroke", "#C8A96E").attr("stroke-width", 1).attr("opacity", 0.3)

        // Free throw circle
        svg.append("circle")
            .attr("cx", xScale(0)).attr("cy", yScale(140))
            .attr("r", xScale(60) - xScale(0))
            .attr("fill", "none")
            .attr("stroke", "#C8A96E").attr("stroke-width", 1).attr("opacity", 0.3)

        // Corner three lines - left
        svg.append("line")
            .attr("x1", xScale(-220)).attr("y1", yScale(-50))
            .attr("x2", xScale(-220)).attr("y2", yScale(90))
            .attr("stroke", "#C8A96E").attr("stroke-width", 1).attr("opacity", 0.3)

        // Corner three lines - right  
        svg.append("line")
            .attr("x1", xScale(220)).attr("y1", yScale(-50))
            .attr("x2", xScale(220)).attr("y2", yScale(90))
            .attr("stroke", "#C8A96E").attr("stroke-width", 1).attr("opacity", 0.3)
        
        // Draw the three point line
        const arc = d3.arc()
            .innerRadius(0)
            .outerRadius(xScale(238) - xScale(0))
            .startAngle(-Math.PI / 2)
            .endAngle(Math.PI / 2)
        
        svg.append("path")
            .attr("d", arc as any)
            .attr("transform", `translate(${xScale(0)}, ${yScale(0)})`)
            .attr("fill", "none")
            .attr("stroke", "#C8A96E").attr("stroke-width", 1).attr("opacity", 0.3)

        
        // Plot Shot Dots
        if (mode === "dots") {
        svg.selectAll("circle.shot")
            .data(shots)
            .enter()
            .append("circle")
            .attr("class", "shot")
            .attr("cx", d => xScale(d.locX))
            .attr("cy", d => yScale(d.locY))
            .attr("r", 3)
            .attr("fill", d => d.shotMade ? "#4ADE80" : "#FF4D00")
            .attr("opacity", d => d.shotMade ? 0.8 : 0.6)
        }

        if (mode === "hexbin") {
            const colorScale = d3.scaleSequential()
                .domain([0, 1])
                .interpolator(d3.interpolateRgb("#FF4D00", "#4ADE80"))
            
            const hexbinGenerator = hexbin<ShotChart>()
            .x(d => xScale(d.locX))
            .y(d => yScale(d.locY))
            .radius(15)
            
            const bins = hexbinGenerator(shots)

            const maxBinSize = d3.max(bins, d => d.length) ?? 1

            const sizeScale = d3.scaleLinear()
                .domain([0, maxBinSize])
                .range([3, 15])  // 12 is your hexbin radius


            svg.selectAll("path.hex")
                .data(bins)
                .enter()
                .append("path")
                .attr("class", "hex")
                .attr("transform", d => `translate(${d.x}, ${d.y})`)
                .attr("d", d => hexbinGenerator.hexagon(sizeScale(d.length)))
                .attr("fill", d=> {
                    const made = d.filter((s: ShotChart) => s.shotMade).length
                    const efficiency = made / d.length
                    return colorScale(efficiency)

                })
                .attr("opacity", 0.85)
            
        }


    }, [shots, mode])

    return (
        <svg ref={svgRef} width={500} height={470} />
    )
}