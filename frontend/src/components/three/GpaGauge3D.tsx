import { useRef, useMemo } from 'react'
import { Canvas, useFrame } from '@react-three/fiber'
import { Text, OrbitControls } from '@react-three/drei'
import * as THREE from 'three'

interface GpaGaugeProps {
  gpa: number
  maxGpa?: number
}

function GaugeArc({ startAngle, endAngle, color, radius, thickness }: {
  startAngle: number; endAngle: number; color: string; radius: number; thickness: number
}) {
  const shape = useMemo(() => {
    const s = new THREE.Shape()
    const segments = 48
    const outerR = radius + thickness / 2
    const innerR = radius - thickness / 2
    const arc = endAngle - startAngle

    for (let i = 0; i <= segments; i++) {
      const angle = startAngle + (i / segments) * arc
      const x = Math.cos(angle) * outerR
      const y = Math.sin(angle) * outerR
      if (i === 0) s.moveTo(x, y)
      else s.lineTo(x, y)
    }
    for (let i = segments; i >= 0; i--) {
      const angle = startAngle + (i / segments) * arc
      const x = Math.cos(angle) * innerR
      const y = Math.sin(angle) * innerR
      s.lineTo(x, y)
    }
    s.closePath()
    return s
  }, [startAngle, endAngle, radius, thickness])

  return (
    <mesh rotation={[0, 0, 0]}>
      <extrudeGeometry args={[shape, { depth: 0.2, bevelEnabled: true, bevelThickness: 0.03, bevelSize: 0.03, bevelSegments: 2 }]} />
      <meshStandardMaterial color={color} roughness={0.3} metalness={0.35} />
    </mesh>
  )
}

function Needle({ angle }: { angle: number }) {
  const needleRef = useRef<THREE.Group>(null)
  const targetAngle = useRef(Math.PI)

  targetAngle.current = angle

  useFrame(() => {
    if (needleRef.current) {
      const current = needleRef.current.rotation.z
      needleRef.current.rotation.z += (targetAngle.current - current) * 0.03
    }
  })

  return (
    <group ref={needleRef} position={[0, 0, 0.15]}>
      <mesh>
        <coneGeometry args={[0.04, 1.2, 8]} />
        <meshStandardMaterial color="#1c1917" roughness={0.5} />
      </mesh>
      <mesh position={[0, -0.05, 0]}>
        <sphereGeometry args={[0.08, 16, 16]} />
        <meshStandardMaterial color="#1c1917" roughness={0.5} metalness={0.5} />
      </mesh>
    </group>
  )
}

function Gauge({ gpa, maxGpa }: GpaGaugeProps) {
  const groupRef = useRef<THREE.Group>(null)
  const fraction = Math.min(gpa / maxGpa!, 1)

  // Gauge goes from 135deg to 405deg (left to right, bottom arc)
  const gaugeStart = (3 * Math.PI) / 4
  const gaugeEnd = (9 * Math.PI) / 4
  const gaugeRange = gaugeEnd - gaugeStart

  // Needle angle (rotated 90deg because cone points up)
  const needleAngle = gaugeStart + fraction * gaugeRange - Math.PI / 2

  // Color segments: red -> yellow -> green
  const bgEnd = gaugeStart + gaugeRange

  return (
    <group ref={groupRef}>
      {/* Background arc (gray) */}
      <GaugeArc startAngle={gaugeStart} endAngle={bgEnd} color="#e7e5e4" radius={1.2} thickness={0.35} />

      {/* Red segment (0-1.0) */}
      <GaugeArc startAngle={gaugeStart} endAngle={gaugeStart + gaugeRange * 0.25} color="#ef4444" radius={1.2} thickness={0.36} />

      {/* Yellow segment (1.0-2.5) */}
      <GaugeArc startAngle={gaugeStart + gaugeRange * 0.25} endAngle={gaugeStart + gaugeRange * 0.625} color="#f59e0b" radius={1.2} thickness={0.36} />

      {/* Green segment (2.5-4.0) */}
      <GaugeArc startAngle={gaugeStart + gaugeRange * 0.625} endAngle={bgEnd} color="#22c55e" radius={1.2} thickness={0.36} />

      {/* Active fill overlay */}
      <mesh position={[0, 0, 0.25]}>
        <GaugeArc
          startAngle={gaugeStart}
          endAngle={gaugeStart + fraction * gaugeRange}
          color={fraction > 0.625 ? '#7c3aed' : fraction > 0.25 ? '#a855f7' : '#c084fc'}
          radius={1.2}
          thickness={0.25}
        />
      </mesh>

      <Needle angle={needleAngle} />

      {/* GPA Text */}
      <Text position={[0, -0.3, 0.3]} fontSize={0.5} color="#7c3aed" anchorX="center" anchorY="middle" font={undefined}>
        {gpa.toFixed(2)}
      </Text>
      <Text position={[0, -0.7, 0.3]} fontSize={0.18} color="#64748b" anchorX="center" anchorY="middle" font={undefined}>
        {'/ ' + maxGpa!.toFixed(1) + ' GPA'}
      </Text>
    </group>
  )
}

export function GpaGauge3D({ gpa, maxGpa = 4.0 }: GpaGaugeProps) {
  return (
    <div className="w-full h-52">
      <Canvas camera={{ position: [0, 0, 3.5], fov: 45 }}>
        <ambientLight intensity={0.6} />
        <directionalLight position={[3, 5, 5]} intensity={0.5} />
        <Gauge gpa={gpa} maxGpa={maxGpa} />
        <OrbitControls enablePan={false} enableZoom={false} enableRotate={false} />
      </Canvas>
    </div>
  )
}
