package com.lib.sdkdemo;


/* added based on sample  */
import com.azure.resourcemanager.network.models.Network;
import com.azure.resourcemanager.network.models.NetworkSecurityGroup;
import com.azure.resourcemanager.resources.models.ResourceGroup;

import com.azure.resourcemanager.AzureResourceManager;
import com.azure.resourcemanager.eventhubs.models.Subnet;
import com.azure.core.management.profile.AzureProfile;
import com.azure.core.management.AzureEnvironment;
import com.azure.core.management.Resource;
import com.azure.core.credential.TokenCredential;
import com.azure.core.http.policy.HttpLogDetailLevel;
import com.azure.core.http.rest.PagedIterable;
import com.azure.identity.DefaultAzureCredentialBuilder;
import java.util.Map;


/* added because of error message in build  */
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.slf4j.impl.StaticLoggerBinder;
import java.util.Optional;

/* Added troubleshooting authentication */
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );


        //Environment Variables

        String clientID = System.getenv("AZURE_CLIENT_ID");
        String tenantID = System.getenv("AZURE_TENANT_ID");
        String clientSectet = System.getenv("AZURE_CLIENT_SECRET");
        
        System.out.println(clientID);
        System.out.println(tenantID);
        System.out.println(clientSectet);
        // Azure SDK client builders accept the credential as a parameter.
        // From https://docs.microsoft.com/en-us/azure/developer/java/sdk/identity
        
        
        SecretClient client = new SecretClientBuilder()
        .vaultUrl("https://libkeys.vault.azure.net")
        .credential(new DefaultAzureCredentialBuilder().build())
        .buildClient();

        AzureResourceManager azureResourceManager = AzureResourceManager.authenticate(
                new DefaultAzureCredentialBuilder().build(),
                new AzureProfile(AzureEnvironment.AZURE))
            .withDefaultSubscription();


        // Print selected subscription
        System.out.println("Subscription: " + azureResourceManager.subscriptionId());
        PagedIterable<ResourceGroup> rgGroups = azureResourceManager.resourceGroups().list();
            for (ResourceGroup rgGroup: rgGroups){
            for (Network virtualNetwork : azureResourceManager.networks().listByResourceGroup(rgGroup.name())) {
                System.out.println("           vNet: " + virtualNetwork);
                Map<String, com.azure.resourcemanager.network.models.Subnet> sbNets = virtualNetwork.subnets();
                for (String sNet: sbNets.keySet()){
                    System.out.println("                   SubNet:" + sNet);
                }
            }
            }

    }
}
