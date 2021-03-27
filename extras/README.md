# MVVM Android extras content

- **architecture-diagram.png** - A diagram simply describes the main parts of the architecture. The source file in the draw.io format can be changed in [Google Drive](https://drive.google.com/drive/u/1/folders/1SkculA_-hh0Is6bfxd8HrgF6Iv6hXQ3s) (if you've got permission).
- **arkitekt_template_example.png** - Screenshot showing where to find action to use our template files.
- **arkitekt_live_templates.zip** - Set of useful live-templates to speed up developing with Arkitekt libraries.

## Arkitekt Live Templates

Import to Android Studio or IntelliJ Idea with simple **File -> Manage IDE Settings -> Import Settings -> select arkitekt_live_templates.zip**
Check out tutorial video how to use them. **Link TODO** 
- **UseCase** use `uc` to create UseCase
- **FlowUseCase** use `fc` to create FlowUseCase
- **XML Layout** use `layout` to create xml layout with variables for **View**, **ViewModel**, **ViewState**  
- **ListAdapter** use `la` to create adapter inheriting from **ListAdapter** with **DiffUtils** callback
- **Arkitekt files** use `screen` to create all files for single screen to fully support Arkitekt MVVM. This includes **Fragment**, **Event**, **FragmentModule**, **View**, **ViewModel**, **ViewModelFactory**, **ViewState**. Use **Option** + **Enter** on class name and `Export from current file` to move it to separate file.
