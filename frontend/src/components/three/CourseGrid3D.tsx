import { useRef, useMemo } from 'react'
import { Canvas, useFrame } from '@react-three/fiber'
import { Text, OrbitControls } from '@react-three/drei'
import * as THREE from 'three'

const deptColors: Record<string, string> = {
  CS: '#7c3aed',
  MATH: '#f43f5e',
  PHY: '#059669',
  ENG: '#d97706',
}

function BookObject({ position, color, code, speed }: {
  position: [number, number, number]
  color: string
  code: string
  speed: number
}) {
  const groupRef = useRef<THREE.Group>(null)

  useFrame((state) => {
    if (groupRef.current) {
      groupRef.current.position.y = position[1] + Math.sin(state.clock.elapsedTime * speed + position[0]) * 0.1
      groupRef.current.rotation.y = Math.sin(state.clock.elapsedTime * 0.3 + position[2]) * 0.15
    }
  })

  return (
    <group ref={groupRef} position={position}>
      <mesh>
        <boxGeometry args={[0.5, 0.7, 0.1]} />
        <meshStandardMaterial color={color} roughness={0.4} metalness={0.3} />
      </mesh>
      <mesh position={[0, 0, 0.06]}>
        <boxGeometry args={[0.45, 0.65, 0.02]} />
        <meshStandardMaterial color="#fefce8" roughness={0.8} />
      </mesh>
      <Text
        position={[0, 0, 0.08]}
        fontSize={0.1}
        color={color}
        anchorX="center"
        anchorY="middle"
        font={undefined}
      >
        {code}
      </Text>
    </group>
  )
}

export function CourseGrid3D() {
  const courses = useMemo(() => {
    const items = [
      { code: 'CS101', dept: 'CS' },
      { code: 'CS201', dept: 'CS' },
      { code: 'MATH1', dept: 'MATH' },
      { code: 'MATH2', dept: 'MATH' },
      { code: 'PHY10', dept: 'PHY' },
      { code: 'CS301', dept: 'CS' },
    ]
    return items.map((item, i) => {
      const col = i % 3
      const row = Math.floor(i / 3)
      return {
        ...item,
        position: [(col - 1) * 1.0, (row - 0.5) * -1.0, 0] as [number, number, number],
        color: deptColors[item.dept] || '#7c3aed',
        speed: 0.5 + Math.random() * 0.3,
      }
    })
  }, [])

  return (
    <div className="w-full h-48 rounded-xl overflow-hidden bg-gradient-to-r from-primary-50 to-accent-50 border border-primary-100">
      <Canvas camera={{ position: [0, 0, 3], fov: 40 }}>
        <ambientLight intensity={0.6} />
        <directionalLight position={[3, 5, 5]} intensity={0.7} />
        <pointLight position={[-2, 2, 1]} intensity={0.3} color="#7c3aed" />
        <pointLight position={[2, -1, 1]} intensity={0.2} color="#f43f5e" />
        {courses.map((course) => (
          <BookObject key={course.code} {...course} />
        ))}
        <OrbitControls enablePan={false} enableZoom={false} autoRotate autoRotateSpeed={0.5} />
      </Canvas>
    </div>
  )
}
