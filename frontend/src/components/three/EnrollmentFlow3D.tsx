import { useRef, useMemo } from 'react'
import { Canvas, useFrame } from '@react-three/fiber'
import * as THREE from 'three'

function StudentNode({ position, color }: {
  position: [number, number, number]
  color: string
}) {
  const meshRef = useRef<THREE.Mesh>(null)

  useFrame((state) => {
    if (meshRef.current) {
      meshRef.current.position.y = position[1] + Math.sin(state.clock.elapsedTime * 0.8 + position[0]) * 0.08
    }
  })

  return (
    <mesh ref={meshRef} position={position}>
      <sphereGeometry args={[0.12, 16, 16]} />
      <meshStandardMaterial color={color} roughness={0.3} metalness={0.5} />
    </mesh>
  )
}

function CourseNode({ position, color }: {
  position: [number, number, number]
  color: string
}) {
  const meshRef = useRef<THREE.Mesh>(null)

  useFrame((state) => {
    if (meshRef.current) {
      meshRef.current.rotation.y = state.clock.elapsedTime * 0.5
      meshRef.current.position.y = position[1] + Math.sin(state.clock.elapsedTime * 0.6 + position[2]) * 0.06
    }
  })

  return (
    <mesh ref={meshRef} position={position}>
      <boxGeometry args={[0.2, 0.25, 0.05]} />
      <meshStandardMaterial color={color} roughness={0.3} metalness={0.4} />
    </mesh>
  )
}

function Particle({ from, to, speed, delay }: {
  from: [number, number, number]
  to: [number, number, number]
  speed: number
  delay: number
}) {
  const meshRef = useRef<THREE.Mesh>(null)

  useFrame((state) => {
    if (meshRef.current) {
      const t = ((state.clock.elapsedTime * speed + delay) % 3) / 3
      meshRef.current.position.x = from[0] + (to[0] - from[0]) * t
      meshRef.current.position.y = from[1] + (to[1] - from[1]) * t + Math.sin(t * Math.PI) * 0.15
      meshRef.current.position.z = from[2] + (to[2] - from[2]) * t
      meshRef.current.scale.setScalar(0.6 + Math.sin(t * Math.PI) * 0.4)
    }
  })

  return (
    <mesh ref={meshRef}>
      <sphereGeometry args={[0.03, 8, 8]} />
      <meshStandardMaterial color="#f43f5e" emissive="#f43f5e" emissiveIntensity={0.5} />
    </mesh>
  )
}

export function EnrollmentFlow3D() {
  const data = useMemo(() => {
    const students = Array.from({ length: 4 }, (_, i) => ({
      position: [-1.5, (i - 1.5) * 0.5, 0] as [number, number, number],
      color: '#7c3aed',
    }))

    const courses = Array.from({ length: 3 }, (_, i) => ({
      position: [1.5, (i - 1) * 0.6, 0] as [number, number, number],
      color: '#f43f5e',
    }))

    const particles: Array<{
      from: [number, number, number]
      to: [number, number, number]
      speed: number
      delay: number
    }> = []

    students.forEach((s, si) => {
      const ci = si % courses.length
      particles.push({
        from: s.position,
        to: courses[ci].position,
        speed: 0.4 + Math.random() * 0.3,
        delay: si * 0.8,
      })
    })

    return { students, courses, particles }
  }, [])

  return (
    <div className="w-full h-48 rounded-xl overflow-hidden bg-gradient-to-r from-primary-50 to-accent-50 border border-primary-100">
      <Canvas camera={{ position: [0, 0, 3.5], fov: 35 }}>
        <ambientLight intensity={0.6} />
        <directionalLight position={[3, 5, 5]} intensity={0.7} />
        <pointLight position={[-2, 2, 1]} intensity={0.3} color="#7c3aed" />
        <pointLight position={[2, -1, 1]} intensity={0.2} color="#f43f5e" />

        {data.students.map((s, i) => (
          <StudentNode key={`s-${i}`} {...s} />
        ))}
        {data.courses.map((c, i) => (
          <CourseNode key={`c-${i}`} {...c} />
        ))}
        {data.particles.map((p, i) => (
          <Particle key={`p-${i}`} {...p} />
        ))}
      </Canvas>
    </div>
  )
}
