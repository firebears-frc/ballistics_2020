# Power Cell Ballistics

Multiple shots are simulated to find the optimal launch velocity.  For each
shot, power cell motion and acceleration are simulated in very short time steps.

![Velocity vs Range][velocity vs range]

## Acceleration of Gravity

Gravity is [not uniform] across the Earth's surface.

Location      | `𝑔` (according to Wolfram Alpha)
--------------|---------------------------------
Minneapolis   | 9.82498 m/s²
Duluth        | 9.82816 m/s²
Detroit       | 9.82025 m/s²
Earth average | 9.80665 m/s²

## Parabolic Trajectory

In a vacuum, the trajectory would be a parabola.  This can be used for _trial
shots_ to find launch velocities aiming above and below the port.

### Velocity for trajectory to apex

```
     _______________
𝑣 = √ 2⋅𝑔⋅𝐇 / sin²𝜃
```

Where:
- 𝐇 is the height at the apex of the parabola
- 𝑣 is projectile velocity (m/s)
- 𝜃 is the launch angle

## Drag Force (Air Resistance)

`𝐷 = ½𝑝⋅𝑣²⋅𝐴⋅𝑐`

Where:
- 𝐷 is the drag force (kg⋅m/s² = N) in opposite direction of velocity
- 𝑝 is the fluid mass density (kg/m³)
  * For air at sea level 15°C, 𝑝 = 1.225 kg/m³
  * For air at 20°C, 1016 hPa, dew point 10°C, 𝑝 = 1.2019 kg/m³
- 𝑣 is projectile velocity (m/s)
- 𝐴 is the reference area (m²)
  * For a sphere, use cross-sectional area, 𝐴 = 𝜋⋅𝑟²
  * For power cell, 𝑟 = 0.0889 m (3.5 in), so 𝐴 = 0.024828666 m²
- 𝑐 is the drag coefficient (unitless), which varies by velocity
  * Experimentally determined, related to Reynolds number

![Drag on a Sphere][drag]

### Reynolds Number

`𝑅 = 𝑣⋅𝑝⋅𝑙 / 𝜇`

Where:
- 𝑅 is the Reynolds number (unitless)
- 𝑙 is the reference length (m)
  * For a sphere, it is the diameter
  * For power cell, 𝑙 = 0.1778 m (7 in)
- 𝜇 is the viscosity coefficient (kg/m⋅s = Pa⋅s)
  * For air at 15°C, it is 1.81 x 10⁻⁵ Pa⋅s
  * For air at 20°C, it is 1.83 x 10⁻⁵ Pa⋅s
  * For air at 25°C, it is 1.85 x 10⁻⁵ Pa⋅s

### Magnus Effect (Backspin)

`𝑀 = ½𝑝⋅𝓿²⋅𝐴⋅𝐿`

Where:
- 𝑀 is the Magnus force (N) in 𝑤̂ ⨯ 𝑣̂ direction
- 𝑣̂ is the velocity vector
- 𝑤̂ is the angular velocity vector
- 𝐿 is the lift coefficient, dependant on spin factor
  * Experimentally determined, 0.22 for a baseball

#### Spin Factor

`𝑆 = 𝑤 / 𝑣`

Where:
- 𝑆 is the spin factor
- 𝑤 is the tangential velocity (m/s)
- 𝑣 is the translational velocity (m/s)


[drag]: https://www.grc.nasa.gov/WWW/k-12/airplane/Images/dragsphere.jpg
[not uniform]: https://agupubs.onlinelibrary.wiley.com/doi/full/10.1002/grl.50838
[velocity vs range]: ./velocity_range_45.svg
