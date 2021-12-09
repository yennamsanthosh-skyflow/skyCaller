import { Skyflow } from 'skyflow-node';

export const skyflowClient = Skyflow.init({
    vaultID: "e728297fbdf846cfacff7fa13adb8b15",
    vaultURL: "https://sb.area51.vault.skyflowapis.dev",
    getBearerToken: () => {
        return new Promise((resolve,reject)=>{
            resolve("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY2MiOiJmNzk0ZmY4NmZiYzgxMWVhYmQ2YzNhOTExNDNlM2Q0MiIsImF1ZCI6Imh0dHBzOi8vbWFuYWdlLnNreWZsb3dhcGlzLmRldiIsImV4cCI6MTYzOTE0MDk4MzIwOCwiaWF0IjoxNjM5MDU0NTgzMjA4LCJpc3MiOiJzYS1hdXRoQG1hbmFnZS5za3lmbG93YXBpcy5kZXYiLCJqdGkiOiJhMzllYmE0ODYxM2E0Y2ZkYWVhNDMxY2IxZTIyMWY0MiIsInN1YiI6ImEyMWE0MDI3MmM5MjQ1YzQ4ODZjNDFlNjU4NDdjMTMxIn0.D9raYOm685aUpH7CcJttgFixK01dNJeBWbsXhluFsZOjzheVpdV2VzDooOolbJjDiLlaGu4epM_OQWeFljIivg9DTSJH0EKC534H2z2Fg4PonHsCaTpb5yBFU5Fk-opNFGEGiZPIhpAJLsVYtCNpVjnYxMD4mGsZiwbKyHCEmpY8-08WtGygUcQB3aKcgZ1fpEM1twSliO6iCmLijMjrO4H3bU9Ff-OntubHz72_fOkvshp9jwbQTKVxKdEoxy2-Sy0XWLzzbbtEYA0ZE7_YnwLSa0g4hx1J5AnOgf5zZG_Pt287owPBxaBm5ogFrhadh7P9hMPmvaoeHYmipUanYQ")
        });
    }
});