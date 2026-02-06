import { useRef, useMemo } from 'react'
import { Canvas, useFrame } from '@react-three/fiber'
import * as THREE from 'three'

function GraduationCap() {
  const groupRef = useRef<THREE.Group>(null)

  useFrame((state) => {
    if (groupRef.current) {
      groupRef.current.rotation.y = state.clock.elapsedTime * 0.4
      groupRef.current.position.y = 0.3 + Math.sin(state.clock.elapsedTime * 0.6) * 0.1
    }
  })

  return (
    <group ref={groupRef} position={[0, 0.3, 0]}>
      {/* Cap board */}
      <mesh position={[0, 0.15, 0]} rotation={[0, Math.PI / 4, 0]}>
        <boxGeometry args={[0.8, 0.04, 0.8]} />
        <meshStandardMaterial color="#3b0764" roughness={0.3} metalness={0.5} />
      </mesh>
      {/* Cap dome */}
      <mesh position={[0, 0, 0]}>
        <cylinderGeometry args={[0.3, 0.35, 0.2, 6]} />
        <meshStandardMaterial color="#581c87" roughness={0.4} metalness={0.3} />
      </mesh>
      {/* Tassel button */}
      <mesh position={[0, 0.17, 0]}>
        <sphereGeometry args={[0.04, 12, 12]} />
        <meshStandardMaterial color="#f43f5e" roughness={0.2} metalness={0.6} />
      </mesh>
      {/* Tassel string */}
      <mesh position={[0.25, 0.05, 0.25]}>
        <cylinderGeometry args={[0.01, 0.01, 0.25, 6]} />
        <meshStandardMaterial color="#f43f5e" roughness={0.3} />
      </mesh>
    </group>
  )
}

function Scroll() {
  const groupRef = useRef<THREE.Group>(null)

  useFrame((state) => {
    if (groupRef.current) {
      groupRef.current.position.y = -0.4 + Math.sin(state.clock.elapsedTime * 0.5 + 1) * 0.08
      groupRef.current.rotation.z = Math.sin(state.clock.elapsedTime * 0.3) * 0.1
    }
  })

  return (
    <group ref={groupRef} position={[0, -0.4, 0.3]} rotation={[0, 0, 0.1]}>
      {/* Scroll body */}
      <mesh>
        <cylinderGeometry args={[0.06, 0.06, 0.7, 16]} />
        <meshStandardMaterial color="#fefce8" roughness={0.6} />
      </mesh>
      {/* Scroll caps */}
      <mesh position={[0, 0.37, 0]}>
        <cylinderGeometry args={[0.08, 0.08, 0.03, 16]} />
        <meshStandardMaterial color="#7c3aed" roughness={0.3} metalness={0.5} />
      </mesh>
      <mesh position={[0, -0.37, 0]}>
        <cylinderGeometry args={[0.08, 0.08, 0.03, 16]} />
        <meshStandardMaterial color="#7c3aed" roughness={0.3} metalness={0.5} />
      </mesh>
    </group>
  )
}

function StarParticle({ position, delay }: {
  position: [number, number, number]
  delay: number
}) {
  const meshRef = useRef<THREE.Mesh>(null)

  useFrame((state) => {
    if (meshRef.current) {
      const t = state.clock.elapsedTime + delay
      meshRef.current.position.y = position[1] + Math.sin(t * 0.8) * 0.3
      meshRef.current.rotation.z = t * 2
      const scale = 0.5 + Math.sin(t * 1.2) * 0.3
      meshRef.current.scale.setScalar(scale)
    }
  })

  return (
    <mesh ref={meshRef} position={position}>
      <octahedronGeometry args={[0.04]} />
      <meshStandardMaterial
        color="#f59e0b"
        emissive="#f59e0b"
        emissiveIntensity={0.5}
        roughness={0.2}
        metalness={0.6}
      />
    </mesh>
  )
}

export function TranscriptScroll3D() {
  const stars = useMemo(() =>
    Array.from({ length: 10 }, (_, i) => ({
      position: [
        (Math.random() - 0.5) * 3,
        (Math.random() - 0.5) * 2,
        (Math.random() - 0.5) * 1.5,
      ] as [number, number, number],
      delay: i * 0.5,
    })),
  [])

  return (
    <div className="w-full h-48 rounded-xl overflow-hidden bg-gradient-to-r from-primary-100 to-accent-50 border border-primary-200">
      <Canvas camera={{ position: [0, 0.5, 3], fov: 35 }}>
        <ambientLight intensity={0.5} />
        <directionalLight position={[3, 5, 5]} intensity={0.7} />
        <pointLight position={[-2, 2, 1]} intensity={0.3} color="#7c3aed" />
        <pointLight position={[2, -1, 1]} intensity={0.2} color="#f43f5e" />
        <GraduationCap />
        <Scroll />
        {stars.map((star, i) => (
          <StarParticle key={i} {...star} />
        ))}
      </Canvas>
    </div>
  )
}
