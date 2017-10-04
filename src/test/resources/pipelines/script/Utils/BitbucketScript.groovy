package pipelines.script.Utils

import groovy.json.JsonOutput

/**
 * Created by dlpj on 07/07/2017.
 */

class Bitbucket implements Serializable {

    private String _bitbucketBaseUrl
    private String _sshBitbucketBaseUrl
    private String _hookEndPartUrl
    private String _project
    private String _repo

    private Script _ctx

    Bitbucket(){
        _bitbucketBaseUrl = "http://repos.bancsabadell.com/bitbucket/rest/api/1.0/projects/"
        _sshBitbucketBaseUrl = "ssh://git@repos.bancsabadell.com:7999"
        _hookEndPartUrl = "/settings/hooks/com.bancsabadell.bitbucket.jenkins.pipeline:jenkins-pipeline-starter/"
        _project = ""
        _repo = ""
        _ctx = null
    }

    def Init(String project, String repo, Script ctx){
        SetProject(project)
        SetRepo(repo)
        SetContext(ctx)
    }

    def SetContext(Script ctx){
        _ctx = ctx
    }

    def SetProject(String project){
        _project = project
    }

    def SetRepo(String repo){
        _repo = repo
    }

    def UpdateJenkinsStarterPluginHookConf(String pipelineName, String token, String jenkinsUrl, String triggerBranches, String jobArgs, String user, String creds){
        String updateCmd = CreateBitbucketHookConfCommand(pipelineName, token, jenkinsUrl, triggerBranches, jobArgs, user, creds)
        String enableCmd = CreateBitbucketHookEnableCommand(user, creds)

        _ctx.sh "${updateCmd}"
        _ctx.sh "${enableCmd}"
    }

    private def CreateBitbucketHookConfCommand(String pipelineName, String token, String jenkinsUrl, String triggerBranches, String jobArgs, String user, String creds){

        def payload = JsonOutput.toJson([jenkinsUrl     : jenkinsUrl,
            pipelineName   : pipelineName,
            token          : token,
            jobArgs        : jobArgs,
            triggerBranches: triggerBranches,
            enabled        : true])

        String apiUrl = GetBitbucketHttpApiSettingsUrlFromSshUrl()
        String cmd = "curl -u ${user}:${creds} -X PUT -H \"Content-Type:application/json\" -d \'${payload}\' ${apiUrl}"
        return cmd
    }

    private def CreateBitbucketHookEnableCommand(String user, String creds){

        String apiUrl = GetBitbucketHttpApiEnableUrlFromSshUrl()
        String cmd = "curl -u ${user}:${creds} -X PUT -H \"Content-Type:application/json\" ${apiUrl}"
        return cmd
    }

    def test(){
        _ctx.sh "ls -la"
    }

    //private functions
    private def GetBitbucketHttpApiEnableUrlFromSshUrl(){
        //example: ssh://git@repos.bancsabadell.com:7999/mexarq/mx-ms-security-btservices.git
        def url = _bitbucketBaseUrl + "/" + _project + "/repos/" + _repo + _hookEndPartUrl + "enabled"

        return url
    }

    private def GetBitbucketHttpApiSettingsUrlFromSshUrl(){
        //example: ssh://git@repos.bancsabadell.com:7999/mexarq/mx-ms-security-btservices.git
        def url = _bitbucketBaseUrl + _project + "/repos/" + _repo + _hookEndPartUrl + "settings"

        return url
    }
}

return new Bitbucket()