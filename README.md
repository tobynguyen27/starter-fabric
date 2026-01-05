# Starter template for Fabric

A ready-to-use template for creating [Fabric](https://fabricmc.net) mods using [Kotlin](https://kotlinlang.org). This project is pre-configured with essential dependencies and code formatting tools to help you start development quickly.

## Features

- Dependencies Included:
  - Fabric API 
  - Mod Menu (Local Runtime)
  - LazyDFU (Local Runtime)

- Code Formatting:
  - Spotless integration with:
    - Kotlin: ktfmt
    - Java: Palantir Java Format 
    - JSON: Biome

## Getting Started

### 1. Configuration

Open `gradle.properties` to customize your mod's identity. You should change the following values to match your project:

```properties
mod_id=examplemod
mod_name=Example Mod
mod_version=1.0.0
mod_description=An example mod
mod_license=CC0-1.0
maven_group=com.example
```

### 2. Update Metadata

Update `src/main/resources/fabric.mod.json` with your personal information (authors, contact info, etc.).

> [!NOTE]
> Fields like id, name, and version are automatically replaced from gradle.properties during the build process.

### 3. Formatter

This project uses [Spotless](https://github.com/diffplug/spotless) for formatting code with a personal config so it might not always work for everyone and every use case.

I suggest you to take a look at it and, if necessary, modify it to suit your coding style.

## License

This template is licensed under CC0 1.0 Universal, meaning you can use it for any purpose without restriction. See the LICENSE file for details.