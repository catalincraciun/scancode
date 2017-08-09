//
//  UIImage+Scalling.swift
//  ScanCode
//
//  Created by Catalin Craciun on 05/02/2017.
//  Copyright © 2017 Catalin Craciun. All rights reserved.
//

import UIKit

extension UIImage {
    class func imageWithImage(image:UIImage, scaledToSize newSize:CGSize) -> UIImage{
        UIGraphicsBeginImageContextWithOptions(newSize, false, 0.0);
        image.draw(in: CGRect(origin: CGPoint.zero, size: CGSize(width: newSize.width, height: newSize.height)))
        let newImage:UIImage = UIGraphicsGetImageFromCurrentImageContext()!
        UIGraphicsEndImageContext()
        return newImage
    }
}
