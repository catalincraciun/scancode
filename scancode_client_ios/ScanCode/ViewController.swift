//
//  ViewController.swift
//  ScanCode
//
//  Created by Catalin Craciun on 05/02/2017.
//  Copyright © 2017 Catalin Craciun. All rights reserved.
//

import UIKit

class ViewController: UIViewController {

    var dataToSave: String = "Catalin;Craciun;19"
    var used: Bool = false
    @IBOutlet weak var userCode: UIImageView!
    @IBOutlet weak var scanResult: UILabel!
    let downloadedCodeKey = "downloaded"
    let imageKey = "codeImage"
    let apiKey = "7D8s2DJK23iD92jdDJksqEQewscxnr24j2Dsncsksddsjejdmnds2"
    
    @IBAction func scanPressed(_ sender: Any) {
        let picker = UIImagePickerController()
        picker.delegate = self
        picker.sourceType = .camera
        picker.cameraDevice = .rear
        picker.showsCameraControls = true
        self.present(picker, animated: true, completion: {
            
        })
    }
    
    func generate() {
        // Do request to server
        let url = URL(string: "http://catalincraciun.com:8080/generateCode?apiKey=\(apiKey)&data=\(dataToSave)")!
        var request = URLRequest(url: url)
        request.httpMethod = "GET"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        let task = URLSession.shared.dataTask(with: request) { data, response, error in
            guard error == nil else {
                NSLog("Error!")
                return
            }
            guard let data = data else {
                NSLog("Data is empty")
                return
            }
            
            let str = String.init(data: data, encoding: String.Encoding.utf8)!
            
            print(str)
            if let json = try! JSONSerialization.jsonObject(with: data, options: []) as?Dictionary<String, Any> {
                let name: String = "image"
                let dataDecoded:NSData = NSData(base64Encoded: json[name] as! String, options: [])!
                let image = UIImage(data: dataDecoded as Data)
                DispatchQueue.main.async {
                    self.userCode.image = image
                }
                UserDefaults.standard.set(UIImagePNGRepresentation(image!), forKey: self.imageKey)
                UserDefaults.standard.set(true, forKey: self.downloadedCodeKey)
            } else {
                NSLog("Couldn't read JSON")
            }
        }
        
        task.resume()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        generate()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }


}

extension ViewController: UINavigationControllerDelegate {
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        
    }
    
}

extension ViewController: UIImagePickerControllerDelegate {
    
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [String : Any]) {
        picker.dismiss(animated: true, completion: nil)
        let image = info[UIImagePickerControllerOriginalImage] as! UIImage
        
        // Do request to server
        let url = URL(string: "http://catalincraciun.com:8080/scanCode")!
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.httpBody = try! JSONSerialization.data(withJSONObject: ["apiKey": apiKey, "image": UIImagePNGRepresentation(UIImage.imageWithImage(image: image, scaledToSize: CGSize(width: image.size.width / 6, height: image.size.height / 6)))?.base64EncodedString()], options: [])
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        let task = URLSession.shared.dataTask(with: request) { data, response, error in
            guard error == nil else {
                NSLog("Error!")
                return
            }
            guard let data = data else {
                NSLog("Data is empty")
                return
            }
            
            let str = String.init(data: data, encoding: String.Encoding.utf8)!
            DispatchQueue.main.async {
                str.replacingOccurrences(of: "image", with: "info")
                self.scanResult.text = str
            }
        }
        
        task.resume()
    }
    
}

