import { useRef, useMemo } from 'react'
import { Canvas, useFrame } from '@react-three/fiber'
import * as THREE from 'three'

function Shape({ position, rotation, speed, color, geometry }: {
  position: [number, number, number];
  rotation: [number, number, number];
  speed: number;
  color: string;
  geometry: 'sphere' | 'octahedron' | 'dodecahedron' | 'icosahedron' | 'torus'
}) {
  const meshRef = useRef<THREE.Mesh>(null)
  const initialY = position[1]

  useFrame((state) => {
    if (meshRef.current) {
      meshRef.current.rotation.x += 0.003 * speed
      meshRef.current.rotation.z += 0.002 * speed
      meshRef.current.position.y = initialY + Math.sin(state.clock.elapsedTime * speed * 0.4 + position[0]) * 0.5
    }
  })

  return (
    <mesh ref={meshRef} position={position} rotation={rotation}>
      {geometry === 'sphere' && <sphereGeometry args={[0.3, 16, 16]} />}
      {geometry === 'octahedron' && <octahedronGeometry args={[0.35]} />}
      {geometry === 'dodecahedron' && <dodecahedronGeometry args={[0.3]} />}
      {geometry === 'icosahedron' && <icosahedronGeometry args={[0.35]} />}
      {geometry === 'torus' && <torusGeometry args={[0.25, 0.1, 12, 24]} />}
      <meshStandardMaterial
        color={color}
        roughness={0.4}
        metalness={0.3}
        transparent
        opacity={0.7}
      />
    </mesh>
  )
}

function Shapes() {
  const shapes = useMemo(() => {
    const purpleCoralPalette = ['#7c3aed', '#a855f7', '#c084fc', '#d8b4fe', '#f43f5e', '#fb7185', '#fda4af', '#9333ea', '#6b21a8', '#e11d48']
    const geometries: Array<'sphere' | 'octahedron' | 'dodecahedron' | 'icosahedron' | 'torus'> = ['sphere', 'octahedron', 'dodecahedron', 'icosahedron', 'torus']
    return Array.from({ length: 25 }, (_, i) => ({
      position: [
        (Math.random() - 0.5) * 18,
        (Math.random() - 0.5) * 12,
        (Math.random() - 0.5) * 6 - 3,
      ] as [number, number, number],
      rotation: [
        Math.random() * Math.PI,
        Math.random() * Math.PI,
        Math.random() * Math.PI * 0.5,
      ] as [number, number, number],
      speed: 0.3 + Math.random() * 0.8,
      color: purpleCoralPalette[i % purpleCoralPalette.length],
      geometry: geometries[i % geometries.length],
    }))
  }, [])

  return (
    <>
      {shapes.map((shape, i) => (
        <Shape key={i} {...shape} />
      ))}
    </>
  )
}

export function FloatingShapes() {
  return (
    <div className="absolute inset-0 -z-10">
      <Canvas camera={{ position: [0, 0, 7], fov: 55 }} style={{ background: 'transparent' }}>
        <ambientLight intensity={0.5} />
        <directionalLight position={[5, 5, 5]} intensity={0.3} />
        <pointLight position={[-4, 3, 4]} intensity={0.3} color="#7c3aed" />
        <pointLight position={[4, -3, 4]} intensity={0.2} color="#f43f5e" />
        <fog attach="fog" args={['#faf5ff', 5, 16]} />
        <Shapes />
      </Canvas>
    </div>
  )
}
