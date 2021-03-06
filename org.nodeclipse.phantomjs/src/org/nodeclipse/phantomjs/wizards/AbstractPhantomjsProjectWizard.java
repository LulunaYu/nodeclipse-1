package org.nodeclipse.phantomjs.wizards;

import org.nodeclipse.phantomjs.nature.PhantomjsNature;
import org.nodeclipse.ui.wizards.AbstractNodeProjectWizard;

/**
 * AbstractPhantomjsProjectWizard (copied AbstractNodeProjectWizard), then made as subclass
 * @author Paul Verest
 */

@SuppressWarnings("restriction")
public abstract class AbstractPhantomjsProjectWizard extends AbstractNodeProjectWizard {
//extends Wizard implements INewWizard {
	
    //+ to let overriding
	@Override
    protected String getProjectNature(){
		return PhantomjsNature.NATURE_ID;    	
    }
	

//    private IWorkbench workbench;
//    private IStructuredSelection selection;
//
//    private IProject newProject;
//
//    public AbstractPhantomjsProjectWizard() {
//        setNeedsProgressMonitor(true);
//    }
//
//    @Override
//    public void init(IWorkbench workbench, IStructuredSelection selection) {
//        this.workbench = workbench;
//        this.selection = selection;
//    }
//
//    public IWorkbench getWorkbench() {
//        return workbench;
//    }
//
//    protected IStructuredSelection getSelection() {
//        return selection;
//    }
//
//    @Override
//    public boolean performFinish() {
//        newProject = createNewProject();
//        if (newProject == null) {
//            return false;
//        }
//        
//        updatePerspective();
//        selectAndReveal();
//        return true;
//    }
//    
//    protected abstract IProject createNewProject();
//    
//    protected IProjectDescription createProjectDescription(IProject newProjectHandle, URI location) {
//		IWorkspace workspace = ResourcesPlugin.getWorkspace();
//		final IProjectDescription description = workspace
//				.newProjectDescription(newProjectHandle.getName());
//		description.setLocationURI(location);
//		String[] natures = description.getNatureIds();
//		String[] newNatures = new String[natures.length + 2];
//		System.arraycopy(natures, 0, newNatures, 0, natures.length);
//		newNatures[natures.length] = PhantomjsNature.NATURE_ID;
//		newNatures[natures.length+1] = JavaScriptCore.NATURE_ID;
//		description.setNatureIds(newNatures);    	
//		
//		return description;
//    }
//
//    protected void generateTemplates(String path, IProject projectHandle) throws CoreException {
//		Bundle bundle = Activator.getDefault().getBundle();
//		if (bundle == null) {
//			throw new CoreException(new Status(IStatus.ERROR,
//					Activator.PLUGIN_ID, "bundle not found"));
//		}
//				
//		try {
//			URL location = FileLocator.toFileURL(bundle.getEntry("/"));
//			File templateRoot = new File(location.getPath(), path);
//			LogUtil.info("templateRoot: " + templateRoot.getAbsolutePath());
//			
//			RelativityFileSystemStructureProvider structureProvider = new RelativityFileSystemStructureProvider(
//					templateRoot);
//			ImportOperation operation = new ImportOperation(
//					projectHandle.getFullPath(), templateRoot,
//					structureProvider, new IOverwriteQuery() {
//						public String queryOverwrite(String pathString) {
//							return ALL;
//						}
//					}, structureProvider.getChildren(templateRoot));
//
//			operation.setContext(getShell());
//			operation.run(null);
//		} catch (Exception e) {
//			throw new CoreException(new Status(IStatus.ERROR,
//					Activator.PLUGIN_ID, e.getLocalizedMessage()));
//		}
//	}
//
//	protected void rewriteFile(String filename, IProject projectHandle)
//			throws CoreException {
//		String newLine = System.getProperty("line.separator");
//		IFile readme = projectHandle.getFile(filename);
//		if (!readme.exists()) {
//			throw new CoreException(new Status(IStatus.ERROR,
//					Activator.PLUGIN_ID, filename + "not found"));
//		}
//		InputStreamReader ir = new InputStreamReader(readme.getContents());
//		BufferedReader br = new BufferedReader(ir);
//		StringBuilder sb = new StringBuilder();
//		String line;
//		try {
//			while ((line = br.readLine()) != null) {
//				if (line.contains("${projectname}")) {
//					line = line.replace("${projectname}",
//							projectHandle.getName());
//				}
//				sb.append(line);
//				sb.append(newLine);
//			}
//			ByteArrayInputStream source = new ByteArrayInputStream(sb
//					.toString().getBytes());
//			readme.setContents(source, true, true, null);
//		} catch (IOException e) {
//			throw new CoreException(new Status(IStatus.ERROR,
//					Activator.PLUGIN_ID, "Cannot read " + filename));
//		} finally {
//			try {
//				ir.close();
//				br.close();
//			} catch (IOException e) {
//			}
//			ir = null;
//			br = null;
//		}
//	}
//
//	protected void runJSHint(IProject projectHandle) throws CoreException {
//		String builderId = "com.eclipsesource.jshint.ui.builder";
//		IProjectDescription description = projectHandle.getDescription();
//
//		if (!containsBuildCommand(description, builderId)) {
//			addBuildCommand(description, builderId);
//			projectHandle.setDescription(description, null);
//		}
//
//		triggerClean(projectHandle, builderId);
//	}
//	
//	protected boolean isExistsProjectFolder(IProjectDescription description) {
//		URI location = description.getLocationURI();
//		String name = description.getName();
//		
//		File folder = null;
//		if(location != null) {
//			folder = FileUtil.toPath(FileUtil.canonicalURI(location)).toFile();
//		} else {
//			Workspace workspace = (Workspace)ResourcesPlugin.getWorkspace();
//			folder = workspace.getRoot().getLocation().append(name).toFile();
//		}
//		
//		if(folder.exists()) {
//			if(folder.isDirectory()) {
//				File[] files = folder.listFiles();
//				if(files.length == 0) {
//					return false;
//				} else {
//					return true;
//				}
//			} else {
//				return true;
//			}
//		} else {
//			return false;
//		}
//	}
//	
//	protected boolean containsBuildCommand(IProjectDescription description,
//			String builderId) {
//		for (ICommand command : description.getBuildSpec()) {
//			if (command.getBuilderName().equals(builderId)) {
//				return true;
//			}
//		}
//		return false;
//	}
//
//	protected void addBuildCommand(IProjectDescription description, String builderId) {
//		ICommand[] oldCommands = description.getBuildSpec();
//		ICommand[] newCommands = new ICommand[oldCommands.length + 1];
//		System.arraycopy(oldCommands, 0, newCommands, 0, oldCommands.length);
//		newCommands[newCommands.length - 1] = createBuildCommand(description, builderId);
//		description.setBuildSpec(newCommands);
//	}
//
//	protected ICommand createBuildCommand(IProjectDescription description, String builderId) {
//		ICommand command = description.newCommand();
//		command.setBuilderName(builderId);
//		return command;
//	}
//
//	protected void triggerClean(IProject project, String builderName) throws CoreException {
//		project.build(IncrementalProjectBuilder.CLEAN_BUILD, builderName, null,	null);
//	}
//
//	private void selectAndReveal() {
//        BasicNewResourceWizard.selectAndReveal(newProject, workbench.getActiveWorkbenchWindow());
//    }
//
//    protected void updatePerspective() {
//        IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
//        IPerspectiveRegistry reg = WorkbenchPlugin.getDefault().getPerspectiveRegistry();
//        PerspectiveDescriptor rtPerspectiveDesc = (PerspectiveDescriptor) reg.findPerspectiveWithId(NodePerspective.ID);
//        // Now set it as the active perspective.
//        if (window != null) {
//            IWorkbenchPage page = window.getActivePage();
//            page.setPerspective(rtPerspectiveDesc);
//        }
//    }
}

